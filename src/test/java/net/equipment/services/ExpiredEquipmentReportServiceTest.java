package net.equipment.services;

import net.equipment.models.Equipment;
import net.equipment.models.EquipmentCategory;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExpiredEquipmentReportServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @InjectMocks
    private ExpiredEquipmentReportService expiredEquipmentReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExpiredEquipmentReport() {
        Long companyId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // Моделируем категорию оборудования с истечением через 12 месяцев
        EquipmentCategory category1 = new EquipmentCategory();
        category1.setCategoryId(1L);
        category1.setExpirationPeriodInMonths(12);

        // Моделируем оборудование, созданное 13 месяцев назад (должно быть просрочено)
        Equipment expiredEquipment1 = new Equipment();
        expiredEquipment1.setEquipmentId(1L);
        expiredEquipment1.setCreatedAt(now.minusMonths(13));

        // Моделируем оборудование, созданное 10 месяцев назад (не просрочено)
        Equipment validEquipment1 = new Equipment();
        validEquipment1.setEquipmentId(2L);
        validEquipment1.setCreatedAt(now.minusMonths(10));

        // Настраиваем поведение моков
        when(equipmentCategoryRepository.findAll()).thenReturn(Collections.singletonList(category1));
        when(equipmentRepository.findByCategoryIdAndCompanyId(1L, companyId))
                .thenReturn(Arrays.asList(expiredEquipment1, validEquipment1));

        // Выполняем метод и проверяем результаты
        List<Equipment> expiredEquipment = expiredEquipmentReportService.expiredEquipmentReport(companyId);

        // Должно вернуться только просроченное оборудование
        assertEquals(1, expiredEquipment.size());
        assertEquals(expiredEquipment1.getEquipmentId(), expiredEquipment.get(0).getEquipmentId());
    }

    @Test
    void testExpiredEquipmentReport_NoExpiredEquipment() {
        Long companyId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // Моделируем категорию оборудования с истечением через 6 месяцев
        EquipmentCategory category2 = new EquipmentCategory();
        category2.setCategoryId(2L);
        category2.setExpirationPeriodInMonths(6);

        // Моделируем оборудование, созданное 3 месяца назад (не просрочено)
        Equipment validEquipment2 = new Equipment();
        validEquipment2.setEquipmentId(3L);
        validEquipment2.setCreatedAt(now.minusMonths(3));

        // Настраиваем поведение моков
        when(equipmentCategoryRepository.findAll()).thenReturn(Collections.singletonList(category2));
        when(equipmentRepository.findByCategoryIdAndCompanyId(2L, companyId))
                .thenReturn(Collections.singletonList(validEquipment2));

        // Выполняем метод и проверяем результаты
        List<Equipment> expiredEquipment = expiredEquipmentReportService.expiredEquipmentReport(companyId);

        // Список просроченного оборудования должен быть пустым
        assertEquals(0, expiredEquipment.size());
    }
}
