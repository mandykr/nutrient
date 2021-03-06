package mandykr.nutrient.controller.combination;

import lombok.RequiredArgsConstructor;
import mandykr.nutrient.dto.combination.*;
import mandykr.nutrient.service.combination.CombinationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static mandykr.nutrient.util.ApiUtils.ApiResult;
import static mandykr.nutrient.util.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CombinationController {
    private final CombinationService combinationService;

    /**
     * 검색조건을 전달받아 영양제 조합 목록을 페이징으로 반환한다.
     *
     */
    @GetMapping("/combinations")
    public ApiResult<Page<CombinationDto>> getCombinations(
            @RequestBody List<CombinationConditionSupplement> conditionSupplementList,
            @RequestBody List<CombinationConditionCategory> conditionCategoryList,
            Pageable pageable) {
        CombinationSearchCondition condition = new CombinationSearchCondition(conditionSupplementList, conditionCategoryList);
        return success(combinationService.getCombinations(condition, pageable));
    }

    /**
     * 영양제 번호 List, Caption을 입력받아 영양제 조합을 저장한다.
     */
    @PostMapping("/combinations")
    public ApiResult<CombinationDetailDto> createCombination(
            @RequestBody @Valid CombinationCreateRequest combinationCreateRequest) {
        return success(combinationService.createCombination(combinationCreateRequest));
    }

    /**
     * 영양제 번호 List, Caption을 입력받아 영양제 조합을 수정한다.
     */
    @PutMapping("/combination/{id}")
    public ApiResult<CombinationDetailDto> updateCombination(
            @PathVariable("id") Long id,
            @RequestBody @Valid CombinationUpdateRequest request) {
        return success(combinationService.updateCombination(id, request));
    }

    /**
     * 영양제조합 ID로 영양제 조합을 삭제한다.
     */
    @DeleteMapping("/combination/{id}")
    public void deleteCombination(
        @PathVariable("id") Long id) {
        combinationService.deleteCombination(id);
    }
}
