package vietnamworks.com.jobapp.entities;

import R.helper.BindField;
import R.helper.EntityX;

/**
 * Created by duynk on 2/23/16.
 *
 */
public class SearchHistoryEntity extends EntityX {
    public SearchHistoryEntity() {super();}

    @BindField("title") String jobTitle;
    @BindField("category") String category;
    @BindField("location") String location;
    @BindField("level") String level;
    @BindField("min_salary") Integer minSalary;
    @BindField("summary") String summary;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object e) {
        if (e instanceof SearchHistoryEntity) {
            SearchHistoryEntity ele = (SearchHistoryEntity)e;
            return jobTitle.equalsIgnoreCase(ele.getJobTitle())
                    && category.equalsIgnoreCase(ele.getCategory())
                    && location.equalsIgnoreCase(ele.getLocation())
                    && minSalary.intValue() == ele.minSalary.intValue();
        }
        return false;
    }
}
