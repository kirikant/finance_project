package mailScheduler.service;

import mailScheduler.dto.ReportParamDto;
import mailScheduler.entity.ReportParamEntity;
import mailScheduler.repositories.AccountRepo;
import mailScheduler.repositories.CategoryRepo;
import mailScheduler.util.convertors.RepParDtoToEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportParamService {

    private final RepParDtoToEntity repParDtoToEntity;
    private final CrossServiceGetter crossServiceGetter;
    private final AccountRepo accountRepo;
    private final CategoryRepo categoryRepo;

    public ReportParamService(RepParDtoToEntity repParDtoToEntity, CrossServiceGetter crossServiceGetter, AccountRepo accountRepo, CategoryRepo categoryRepo) {
        this.repParDtoToEntity = repParDtoToEntity;
        this.crossServiceGetter = crossServiceGetter;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
    }

    @Transactional
    public ReportParamEntity save(ReportParamDto reportParamDto, String token){

        ReportParamEntity reportParamEntity = repParDtoToEntity.convert(reportParamDto);
        reportParamEntity.getAccounts().forEach(x -> crossServiceGetter.checkAccountPermission(x.getUuid(), token));

        if (reportParamEntity.getAccounts() != null) reportParamEntity.getAccounts().forEach(x -> {
            if (accountRepo.findById(x.getUuid()).isEmpty()) accountRepo.save(x.getUuid());
        });
        if (reportParamEntity.getCategories() != null) reportParamEntity.getCategories().forEach(x -> {
            if (categoryRepo.findById(x.getUuid()).isEmpty()) categoryRepo.save(x.getUuid());
        });

        return reportParamEntity;
    }
}
