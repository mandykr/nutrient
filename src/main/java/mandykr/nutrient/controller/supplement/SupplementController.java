package mandykr.nutrient.controller.supplement;

import lombok.RequiredArgsConstructor;
import mandykr.nutrient.dto.supplement.*;
import mandykr.nutrient.security.JwtAuthentication;
import mandykr.nutrient.security.JwtAuthenticationToken;
import mandykr.nutrient.service.supplement.SupplementService;
import mandykr.nutrient.util.ApiUtils.ApiResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static mandykr.nutrient.util.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplement")
public class SupplementController {
    private final SupplementService supplementService;


    /**
     * 영양제 전체 조회
     * (이름으로 조회, 카테고리로 조회)
     * @return
     */
    @GetMapping("")
    public ApiResult<Page<SupplementSearchResponse>> getSupplementList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String supplementName,
            final Pageable pageable){
        SupplementSearchRequest supplementSearch = new SupplementSearchRequest(categoryId, supplementName);
        return success(supplementService.getSupplementList(supplementSearch, pageable));
    }

    /**
     * 영양제 단건 조회
     * @param supplementId
     * @return
     */
    @GetMapping("/{supplementId}")
    public ApiResult<SupplementResponse> getSupplement(
            @PathVariable Long supplementId,
            @AuthenticationPrincipal JwtAuthentication authentication){

        return success(supplementService.getSupplement(supplementId));
    }

    /**
     * 영양제 등록
     * @param categoryId
     * @param supplementRequest
     * @return
     */
    @PostMapping("/{categoryId}")
    public ApiResult<SupplementResponse> createSupplement(
            @PathVariable Long categoryId,
            @RequestBody @Valid SupplementRequest supplementRequest){
        return success(supplementService.createSupplement(supplementRequest,categoryId));
    }

    /**
     * 영양제 수정
     * @param categoryId
     * @param supplementId
     * @param supplementRequest
     * @return
     */
    @PutMapping("/{categoryId}/{supplementId}")
    public ApiResult<SupplementResponse> updateSupplement(
            @PathVariable Long categoryId,
            @PathVariable Long supplementId,
            @RequestBody @Valid SupplementRequest supplementRequest){
        return success(supplementService.updateSupplement(categoryId,supplementId,supplementRequest));
    }

    /**
     * 영양제 삭제
     * @param supplementId
     */
    @DeleteMapping("/{supplementId}")
    public void deleteSupplement(
            @PathVariable Long supplementId){
        supplementService.deleteSupplement(supplementId);
    }

    /**
     * 영양제 이름으로 아이디 검색
     * @param name
     * @return
     */
    @GetMapping("/combo")
    public ApiResult<List<SupplementSearchComboResponse>> getSupplementSearchCombo(
            @RequestParam(required = true) String name){
        return success(supplementService.getSupplementSearchCombo(new SupplementSearchComboRequest(name)));
    }
}
