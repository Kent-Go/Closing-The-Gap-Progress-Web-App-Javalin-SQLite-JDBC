package app;
import java.util.ArrayList;
public class Persona {
    private int PersonaId;
    private String PersonaName;
    private String PersonaQuote;
    private int PersonaAge;
    private String PersonaCareer;
    private String PersonaEthnicity;
    private String PersonaGender;
    private ArrayList<String> PersonaNeedGoal;
    private ArrayList<String> PersonaSkillExp;

    public Persona(int PersonaId, String PersonaName, String PersonaQuote, int PersonaAge, 
                    String PersonaCareer, String PersonaEthnicity, String PersonaGender) {
        this.PersonaId = PersonaId;
        this.PersonaName = PersonaName;
        this.PersonaQuote = PersonaQuote;
        this.PersonaAge = PersonaAge;
        this.PersonaCareer = PersonaCareer;
        this.PersonaEthnicity = PersonaEthnicity;
        this.PersonaGender = PersonaGender;
    }

    public int getPersonaId() {
        return PersonaId;
    }

    public String getPersonaName() {
        return PersonaName;
    }

    public String getPersonaQuote() {
        return PersonaQuote;
    }

    public int getPersonaAge() {
        return PersonaAge;
    }

    public String getPersonaCareer() {
        return PersonaCareer;
    }

    public String getPersonaEthnicity() {
        return PersonaEthnicity;
    }

    public String getPersonaGender() {
        return PersonaGender;
    }

    public void setNeedGoal(ArrayList<String> NeedGoal){
        this.PersonaNeedGoal = NeedGoal;
    }

    public ArrayList<String> getPersonaNeedGoal() {
        return PersonaNeedGoal;
    }

    public void setSkillExp(ArrayList<String> SkillExp){
        this.PersonaSkillExp = SkillExp;
    }

    public ArrayList<String> getPersonaSkillExp() {
        return PersonaSkillExp;
    }

}
