package cycling;

import java.util.ArrayList;

package cycling;
public class Team{
    private Rider[] riders;
    private String name;
    private Integer id;
    private String description;

    public Team(String name, Rider[] riders, int id, String description) {
        this.name = name;
        this.riders = riders;
        this.id = id;
        this.description = description;
    }

    public Rider[] getRiders() {
        return riders;
    }
    public String getName() {
        return name;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public int getTeamId() {
        return id;
    }
    public String getDescription(){
        return description;
    }
}
