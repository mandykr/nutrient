package mandykr.nutrient.dto.supplement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mandykr.nutrient.entity.SupplementCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplementCategoryDto {
    private Long id;
    private String name;
    private int depth;
    private SupplementParentCategoryDto parentCategory;

    public SupplementCategoryDto(Long id, String name, int depth) {
        this.id = id;
        this.name = name;
        this.depth = depth;
    }

    public static SupplementCategoryDto toCategoryDto(SupplementCategory category) {
        SupplementCategoryDto categoryDto = new SupplementCategoryDto(category.getId(), category.getName(), category.getDepth());
        categoryDto.setParentCategory(category.getParentCategory() == null ?
                null : new SupplementParentCategoryDto(category.getParentCategory().getId()));
        return categoryDto;
    }

    public static SupplementCategoryDto toCategoryDto(Long id, String name, int depth, Long parentId) {
        SupplementCategoryDto categoryDto = new SupplementCategoryDto(id, name, depth);
        categoryDto.setParentCategory(new SupplementParentCategoryDto(parentId));
        return categoryDto;
    }

    public void setParentCategoryById(Long parentId) {
        setParentCategory(new SupplementParentCategoryDto(parentId));
    }

    @Data
    @AllArgsConstructor
    public static class SupplementParentCategoryDto {
        private Long id;
    }
}
