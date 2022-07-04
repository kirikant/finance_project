package mailScheduler.entity;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "uuid")
    private UUID uuid;

    @ManyToMany(mappedBy = "categories")
    private List<ReportParamEntity> reportParamEntities;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<ReportParamEntity> getReportParamEntities() {
        return reportParamEntities;
    }

    public void setReportParamEntities(List<ReportParamEntity> reportParamEntities) {
        this.reportParamEntities = reportParamEntities;
    }
}
