package com.classifier.classifier.rest;

import com.classifier.classifier.dto.CategoryDto;
import com.classifier.classifier.dto.CurrencyDto;
import com.classifier.classifier.exception.ValidationChecker;
import com.classifier.classifier.exception.ValidationException;
import com.classifier.classifier.service.CategoryService;
import com.classifier.classifier.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/classifier")
public class ClassifierController {

    private final CategoryService categoryService;
    private final CurrencyService currencyService;
    private final ValidationChecker validationChecker;

    public ClassifierController(CategoryService categoryService, CurrencyService currencyService,
                                ValidationChecker validationChecker) {
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.validationChecker = validationChecker;
    }

    @PostMapping("/currency")
    public ResponseEntity<?> addCurrency(@RequestBody CurrencyDto currencyDto) throws ValidationException {
        validationChecker.checkSimpleDtoFields(currencyDto);
        currencyService.add(currencyDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currency")
    public ResponseEntity<?> getCurrency(@RequestParam Integer page,
                                 @RequestParam Integer size) throws ValidationException {
        validationChecker.checkPageParams(page, size);
        return ResponseEntity.ok().body(currencyService.get(page, size));
    }

    @PostMapping("/operation/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) throws ValidationException {
        validationChecker.checkSimpleDtoFields(categoryDto);
        categoryService.add(categoryDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/operation/category")
    public ResponseEntity<?> getCategory(@RequestParam Integer page,
                                 @RequestParam Integer size) throws ValidationException {
        validationChecker.checkPageParams(page, size);
        return ResponseEntity.ok().body(categoryService.get(page, size));
    }

}
