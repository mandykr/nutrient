package mandykr.nutrient.repository.combination;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mandykr.nutrient.dto.combination.*;
import mandykr.nutrient.entity.combination.Combination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.Projections.bean;
import static mandykr.nutrient.entity.supplement.QSupplement.supplement;
import static mandykr.nutrient.entity.QSupplementCategory.supplementCategory;
import static mandykr.nutrient.entity.QSupplementCombination.supplementCombination;
import static mandykr.nutrient.entity.combination.QCombination.combination;

@Repository
@RequiredArgsConstructor
public class CombinationRepositoryImpl implements CombinationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CombinationDto> findWithSupplement(Pageable pageable) {
        List<CombinationDto> content = queryFactory
                .from(combination)
                .join(combination.supplementCombinations, supplementCombination)
                .join(supplementCombination.supplement, supplement)
                .join(supplement.supplementCategory, supplementCategory)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(combination.id)
                                .list(
                                        bean(CombinationDto.class,
                                                combination.id,
                                                combination.caption,
                                                combination.rating,
                                                list(
                                                        bean(CombinationDto.SupplementDto.class,
                                                                supplement.id,
                                                                supplement.name,
                                                                supplementCategory.id.as("categoryId"),
                                                                supplementCategory.name.as("categoryName")
                                                        )
                                                ).as("supplementDtoList")
                                        )
                                )
                );


        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<CombinationDto> searchBySupplementList(List<CombinationConditionSupplement> supplementList, Pageable pageable) {
        List<CombinationDto> content = queryFactory
                .from(combination)
                .join(combination.supplementCombinations, supplementCombination)
                .join(supplementCombination.supplement, supplement)
                .join(supplement.supplementCategory, supplementCategory)
                .where(
                        containsSupplements(supplementList)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(combination.id)
                                .list(
                                        bean(CombinationDto.class,
                                                combination.id,
                                                combination.caption,
                                                combination.rating,
                                                list(
                                                        bean(CombinationDto.SupplementDto.class,
                                                                supplement.id,
                                                                supplement.name,
                                                                supplementCategory.id.as("categoryId"),
                                                                supplementCategory.name.as("categoryName")
                                                        )
                                                ).as("supplementDtoList")
                                        )
                                )
                );


        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<CombinationDto> searchByCategoryList(List<CombinationConditionCategory> categoryList, Pageable pageable) {
        List<CombinationDto> content = queryFactory
                .from(combination)
                .join(combination.supplementCombinations, supplementCombination)
                .join(supplementCombination.supplement, supplement)
                .join(supplement.supplementCategory, supplementCategory)
                .where(
                        containsCategories(categoryList)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(combination.id)
                                .list(
                                        bean(CombinationDto.class,
                                                combination.id,
                                                combination.caption,
                                                combination.rating,
                                                list(
                                                        bean(CombinationDto.SupplementDto.class,
                                                                supplement.id,
                                                                supplement.name,
                                                                supplementCategory.id.as("categoryId"),
                                                                supplementCategory.name.as("categoryName")
                                                        )
                                                ).as("supplementDtoList")
                                        )
                                )
                );


        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public CombinationDetailDto searchByCombination(Combination search) {
        List<CombinationDetailDto> combinationDetailDto = queryFactory
                .from(combination)
                .join(combination.supplementCombinations, supplementCombination)
                .join(supplementCombination.supplement, supplement)
                .join(supplement.supplementCategory, supplementCategory)
                .where(
                        containCombination(search)
                ) .transform(
                        groupBy(combination.id)
                                .list(
                                        bean(CombinationDetailDto.class,
                                                combination.id,
                                                combination.caption,
                                                combination.rating,
                                                list(
                                                        bean(CombinationDetailDto.SupplementDto.class,
                                                                supplement.id,
                                                                supplement.name,
                                                                supplementCategory.id.as("categoryId"),
                                                                supplementCategory.name.as("categoryName")
                                                        )
                                                ).as("supplementDtoList")
                                        )
                                )
                );

        return combinationDetailDto.get(0);
    }

    private Predicate containCombination(Combination search) {
        return  combination.id.eq(search.getId());
    }

    private BooleanExpression containsSupplements(List<CombinationConditionSupplement> supplementList) {
        BooleanExpression expression = null;
        if (!supplementList.isEmpty()) {
            List<Long> ids = supplementList.stream().map(CombinationConditionSupplement::getId).collect(Collectors.toList());

            expression = combination.id.in(JPAExpressions
                    .select(supplementCombination.combination.id)
                    .from(supplementCombination)
                    .where(supplementCombination.supplement.id.in(ids))
                    .groupBy(supplementCombination.combination.id)
                    .having(supplementCombination.supplement.id.count().eq((long) ids.size())));
        }
        return expression;
    }

    private BooleanExpression containsCategories(List<CombinationConditionCategory> categoryList) {
        BooleanExpression expression = null;
        if (!categoryList.isEmpty()) {
            List<Long> ids = categoryList.stream().map(CombinationConditionCategory::getId).collect(Collectors.toList());

            expression = combination.id.in(JPAExpressions
                    .select(supplementCombination.combination.id)
                    .from(supplementCombination)
                    .join(supplementCombination.supplement, supplement)
                    .where(supplement.supplementCategory.id.in(ids))
                    .groupBy(supplementCombination.combination.id)
                    .having(supplement.supplementCategory.id.count().goe((long) ids.size())));
        }
        return expression;
    }

}
