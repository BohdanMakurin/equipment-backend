package net.equipment.services;

import lombok.RequiredArgsConstructor;
import net.equipment.models.Equipment;
import net.equipment.models.EquipmentCategory;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for generating a report of expired equipment based on category expiration periods.
 */
@RequiredArgsConstructor
@Service
public class ExpiredEquipmentReportService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    /**
     * Retrieves a list of equipment that has expired based on its category's expiration period for a specific company.
     * The expiration period is defined in months in the {@link EquipmentCategory}.
     *
     * @param companyId the ID of the company for which to generate the report
     * @return a list of {@link Equipment} that has expired for the specified company
     */
    public List<Equipment> expiredEquipmentReport(Long companyId){

        LocalDateTime currentDate = LocalDateTime.now();
        List<EquipmentCategory> categories = equipmentCategoryRepository.findAll();

        List<Equipment> expiredEquipment = new ArrayList<>();

        for(EquipmentCategory category: categories){

            int expirationPeriodInMonths = category.getExpirationPeriodInMonths();
            LocalDateTime expirationDate = currentDate.minusMonths(expirationPeriodInMonths);
            List<Equipment> equipmentInCategoryAndCompany = equipmentRepository.findByCategoryIdAndCompanyId(category.getCategoryId(), companyId);

            for(Equipment equipment: equipmentInCategoryAndCompany ){
                if(equipment.getCreatedAt().isBefore(expirationDate)){
                    expiredEquipment.add(equipment);
                }
            }
        }
        return expiredEquipment;
    }
}