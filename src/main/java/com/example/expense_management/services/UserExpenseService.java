package com.example.expense_management.services;

import com.example.expense_management.dto.UserExpenseRequest;
import com.example.expense_management.dto.UserExpenseResponse;
import com.example.expense_management.dto.UserExpenseStatisticalByCategoryResponse;
import com.example.expense_management.dto.UserExpenseStatisticalByTimeResponse;
import com.example.expense_management.models.ExpenseCategories;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.UserExpenses;
import com.example.expense_management.repositories.ExpenseCategoryRepository;
import com.example.expense_management.repositories.UserExpensesRepository;
import com.example.expense_management.repositories.UserRepository;
import com.example.expense_management.repositories.spec.MySpecification;
import com.example.expense_management.repositories.spec.SearchCriteria;
import com.example.expense_management.repositories.spec.SearchOperation;
import com.example.expense_management.repositories.spec.UserExpenseSpecification;
import com.example.expense_management.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.example.expense_management.utils.Utils.convertToTimestamp;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserExpenseService {

    private final UserExpensesRepository userExpensesRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ExpenseCategoryService expenseCategoryService;

    public ResponseEntity<ResponseObject> createUserExpense(UserExpenseRequest userExpenseRequest) {
        ExpenseCategories expenseCategories = null;
        if(userExpenseRequest.getCategoryId() != null) {
            expenseCategories = expenseCategoryRepository.findById(userExpenseRequest.getCategoryId()).orElse(null);
            if (expenseCategories == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("fail", "Expenditure category does not exist", ""));
            }
        }

        Integer userId = UserUtil.getCurrentUserId();
        log.info("userId: " + userId);
        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You cannot create expenses for others", ""));
        }
        // hãy dùng builder để tạo đối tượng UserExpenses
        UserExpenses userExpenses = new UserExpenses();
        userExpenses.setExpenseName(userExpenseRequest.getName());
        userExpenses.setAmount(userExpenseRequest.getAmount());
        userExpenses.setNote(userExpenseRequest.getNote());
        userExpenses.setExpenseCategory(expenseCategories);
        userExpenses.setCategoryId(userExpenseRequest.getCategoryId());
        userExpenses.setExpenseDate(userExpenseRequest.getExpenseDate() == null || userExpenseRequest.getExpenseDate().isEmpty() ? new Date() : convertToTimestamp(userExpenseRequest.getExpenseDate()));
        userExpenses.setUserEntity(userRepository.findById(userId).get());
        userExpensesRepository.save(userExpenses);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Create successful spending", userExpensesRepository.save(userExpenses)));

    }

    public ResponseEntity<ResponseObject> getAllUserExpenses(int page, int size, String startDate, String endDate, String name, String note, String categoryName) {

        Specification<UserExpenses> spec = where(null);

        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.desc("expenseDate")
                )
        );

        if (!startDate.isEmpty()) {
            LocalDate date = LocalDate.parse(startDate);
            MySpecification esFoodStartDate = new MySpecification();
            esFoodStartDate.add(new SearchCriteria("expenseDate", date, SearchOperation.DATE_START));
            spec = spec.and(esFoodStartDate);
        }

        if (!endDate.isEmpty()) {
            LocalDate date = LocalDate.parse(endDate);
            MySpecification esFoodEndDate = new MySpecification();
            esFoodEndDate.add(new SearchCriteria("expenseDate", date, SearchOperation.DATE_END));
            spec = spec.and(esFoodEndDate);
        }

        if (!name.isEmpty()) {
            spec = spec.and(UserExpenseSpecification.hasNameLike(name));
        }

        if (!note.isEmpty()) {
            spec = spec.and(UserExpenseSpecification.hasNoteLike(note));
        }

        if (!categoryName.isEmpty()) {
            spec = spec.and(UserExpenseSpecification.hasCategoryNameLike(categoryName));
        }

        Integer userId = UserUtil.getCurrentUserId();

        MySpecification esEmployeeCode = new MySpecification();
        esEmployeeCode.add(new SearchCriteria("userId", userId, SearchOperation.EQUAL));
        spec = spec.and(esEmployeeCode);


        Page<UserExpenses> pageFood;
        pageFood = userExpensesRepository.findAll(spec, paging);

        return getResponseObjectResponseEntity(pageFood);
    }

    private ResponseEntity<ResponseObject> getResponseObjectResponseEntity(Page<UserExpenses> data) {
        Map<String, Object> response = new HashMap<>();
        List<UserExpenses> userExpensesList = Arrays.asList(modelMapper.map(data.getContent(), UserExpenses[].class));
        List<UserExpenseResponse> userExpenseResponseList = new ArrayList<>();
        for (UserExpenses userExpenses : userExpensesList) {
            ExpenseCategories expenseCategories = null;
            if(userExpenses.getCategoryId() != null) {
                expenseCategories = expenseCategoryRepository.findById(userExpenses.getCategoryId()).orElse(null);
            }
            assert expenseCategories != null;
            UserExpenseResponse userExpenseResponse = UserExpenseResponse.builder()
                    .id(userExpenses.getId())
                    .expenseName(userExpenses.getExpenseName())
                    .amount(userExpenses.getAmount())
                    .note(userExpenses.getNote())
                    .categoryName(expenseCategories != null ? expenseCategories.getCategoryName() : "")
                    .categoryId(expenseCategories != null ? expenseCategories.getId() : null)
                    .createdAt(userExpenses.getCreatedAt())
                    .updatedAt(userExpenses.getUpdatedAt())
                    .expenseDate(userExpenses.getExpenseDate())
                    .build();
            userExpenseResponseList.add(userExpenseResponse);
        }
        response.put("currentPage", data.getNumber());
        response.put("totalItems", data.getTotalElements());
        response.put("totalPages", data.getTotalPages());
        response.put("items", userExpenseResponseList);
        log.info("Get Food by name use paging successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
    }

    public ResponseEntity<ResponseObject> getUserExpenseDetail(Integer id) {
        Integer currentUserId = UserUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't see this spending information", ""));
        }
        Optional<UserExpenses> userExpenses = userExpensesRepository.findUserExpensesByIdAndUserEntity(id, userRepository.findById(currentUserId).get());
        return userExpenses.map(expenses -> ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", expenses))).orElseGet(() -> ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("error", "Not found", "")));
    }

    public ResponseEntity<ResponseObject> getListUserExpenseByCategoryId(Integer categoryId, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("createdAt")
                )
        );
        Integer currentUserId = UserUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't see this spending information", ""));
        }
        Page<UserExpenses> userExpenses;
        userExpenses = userExpensesRepository.findUserExpensesByCategoryIdAndUserId(categoryId, currentUserId, paging);
        return getResponseObjectResponseEntity(userExpenses);
    }

    public ResponseEntity<ResponseObject> getUserExpenseStatisticalByCategoryAndTimeRange(String startDate, String endDate) {

        Integer currentUserId = UserUtil.getCurrentUserId();
        long totalAmount = 0;
        int totalExpense = 0;
        List<UserExpenses> listUserExpensesResponse = new ArrayList<>();


        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't see this spending information", ""));
        }

        listUserExpensesResponse = userExpensesRepository.findAllByUserIdAndCreatedAtBetween(currentUserId, convertToTimestamp(startDate), convertToTimestamp(endDate));

        List<ExpenseCategories> allCategories = expenseCategoryRepository.findAll();
        List<ExpenseCategories> expenseCategoriesListResponse = new ArrayList<>();
        for (ExpenseCategories expenseCategories : allCategories) {
            List<UserExpenses> userExpensesList = new ArrayList<>();
            userExpensesList = listUserExpensesResponse.stream()
                    .filter(userExpenses -> userExpenses.getCategoryId() != null && userExpenses.getCategoryId().equals(expenseCategories.getId()))
                    .toList();
            if (!userExpensesList.isEmpty()) {
                expenseCategories.setUserExpensesList(userExpensesList);
                for (UserExpenses userExpenses : userExpensesList) {
                    listUserExpensesResponse.add(userExpenses);
                    totalAmount += userExpenses.getAmount();
                    totalExpense++;
                }
                expenseCategoriesListResponse.add(expenseCategories);
            }
        }

        listUserExpensesResponse.sort(Comparator.comparing(UserExpenses::getCreatedAt));
        Map<String, Object> response = new HashMap<>();

        UserExpenseStatisticalByCategoryResponse userExpenseStatisticalByTimeResponse = UserExpenseStatisticalByCategoryResponse.builder()
                .totalAmount(totalAmount)
                .totalExpense(totalExpense)
                .totalCategory(expenseCategoriesListResponse.size())
                .userExpenses(listUserExpensesResponse)
                .categories(expenseCategoriesListResponse)
                .build();

//        List<UserExpenses> userExpensesList = Arrays.asList(modelMapper.map(data.getContent(), UserExpenses[].class));
        response.put("statistical", userExpenseStatisticalByTimeResponse);
        log.info("Get Food by name use paging successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));

    }

    public ResponseEntity<ResponseObject> getUserExpenseStatisticalByTimeRange(String startDate, String endDate) {
        Integer currentUserId = UserUtil.getCurrentUserId();
        long totalAmount = 0;
        int totalExpense = 0;
        List<UserExpenses> listUserExpensesResponse = new ArrayList<>();

        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't see this spending information", ""));
        }

        listUserExpensesResponse = userExpensesRepository.findAllByUserIdAndCreatedAtBetween(currentUserId, convertToTimestamp(startDate), convertToTimestamp(endDate));
        listUserExpensesResponse.sort(Comparator.comparing(UserExpenses::getCreatedAt));
        totalAmount = listUserExpensesResponse.stream().mapToLong(UserExpenses::getAmount).sum();
        totalExpense = listUserExpensesResponse.size();
        Map<String, Object> response = new HashMap<>();

        UserExpenseStatisticalByTimeResponse userExpenseStatisticalByTimeResponse = UserExpenseStatisticalByTimeResponse.builder()
                .totalAmount(totalAmount)
                .totalExpense(totalExpense)
                .lineItems(listUserExpensesResponse)
                .build();

        response.put("userExpenses", userExpenseStatisticalByTimeResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", response));
    }

    public ResponseEntity<ResponseObject> deleteUserExpense(Integer id) {
        Integer currentUserId = UserUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't delete this spending information", ""));
        }
        Optional<UserExpenses> userExpenses = userExpensesRepository.findUserExpensesByIdAndUserEntity(id, userRepository.findById(currentUserId).get());
        if (userExpenses.isPresent()) {
            userExpensesRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseObject("success", "Deleted spending successfully", ""));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Not found", ""));
        }
    }

    public ResponseEntity<ResponseObject> deleteUserExpenseByListIds(List<Integer> ids) {
        Integer currentUserId = UserUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't delete this spending information", ""));
        }
        for (Integer id : ids) {
            Optional<UserExpenses> userExpenses = userExpensesRepository.findUserExpensesByIdAndUserEntity(id, userRepository.findById(currentUserId).get());
            if (userExpenses.isPresent()) {
                userExpensesRepository.deleteById(id);
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Deleted spending successfully", ""));

    }

    public ResponseEntity<ResponseObject> updateUserExpense(Integer id, UserExpenseRequest userExpenses) {
        Integer currentUserId = UserUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "You don't update this spending information", ""));
        }
        Optional<UserExpenses> userExpensesOptional = userExpensesRepository.findUserExpensesByIdAndUserEntity(id, userRepository.findById(currentUserId).get());
        if (userExpensesOptional.isPresent()) {
            UserExpenses userExpenses1 = userExpensesOptional.get();
            userExpenses1.setExpenseName(userExpenses.getName().isEmpty() ? userExpenses1.getExpenseName() : userExpenses.getName());
            userExpenses1.setAmount(userExpenses.getAmount());
            userExpenses1.setNote(userExpenses.getNote());
            userExpenses1.setCategoryId(userExpenses.getCategoryId());
            userExpenses1.setExpenseCategory(expenseCategoryRepository.findById(userExpenses.getCategoryId()).get());
            userExpenses1.setExpenseDate(userExpenses.getExpenseDate() == null || userExpenses.getExpenseDate().isEmpty() ? userExpenses1.getExpenseDate() : convertToTimestamp(userExpenses.getExpenseDate()));
            userExpensesRepository.save(userExpenses1);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseObject("success", "Updated spending successfully", userExpenses1));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Not found", ""));
        }
    }

}
