package mandykr.nutrient.repository;

import mandykr.nutrient.entity.Supplement;
import mandykr.nutrient.entity.SupplementCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SupplementRepositoryTest {
    @Autowired
    SupplementRepository supplementRepository;
    @Autowired
    SupplementCategoryRepository categoryRepository;

    SupplementCategory parentCategory;
    SupplementCategory category;
    Supplement supplement1;
    Supplement supplement2;

    @BeforeEach
    void setup() {
        parentCategory = new SupplementCategory("오메가369/피쉬오일", 0);
        category = new SupplementCategory("오메가3", 1, parentCategory);
        SupplementCategory saveParentCategory = categoryRepository.save(parentCategory);
        SupplementCategory saveCategory = categoryRepository.save(category);

        supplement1 = Supplement.builder().supplementCategory(category).name("비타민A").prdlstReportNo("1-2-3").ranking(0.0).build();
        supplement2 = Supplement.builder().supplementCategory(category).name("비타민B").prdlstReportNo("2-2-3").ranking(0.0).build();
    }

    @Test
    public void 영양제_등록(){
        //given
        Supplement save = supplementRepository.save(supplement1);
        //when
        Optional<Supplement> findById = supplementRepository.findById(save.getId());
        assertEquals(findById.get().getName(),save.getName());
    }

    @Test
    public void 영양제_전체_조회(){
        //given
        supplementRepository.save(supplement1);
        supplementRepository.save(supplement2);
        //when
        List<Supplement> findAll = supplementRepository.findAll();
        //then
        assertEquals(findAll.size(),2);
    }

    @Test
    public void 영양제_수정(){
        //given
        Supplement supplement = supplementRepository.save(supplement1);
        //when
        supplement.updateNameAndPrdlst("비타민C",null);
        //then
        assertEquals(supplementRepository.findById(supplement.getId()).get().getName(),"비타민C");

    }

}