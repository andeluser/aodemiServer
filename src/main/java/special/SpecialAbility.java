package special;

import java.util.ArrayList;
import java.util.HashMap;

public interface SpecialAbility {

	HashMap<String, Object> specialSkill(String battleID, String playerId) throws Exception;
	HashMap<String, Object> specialSkillSelect(String battleID, String playerId, ArrayList<Object> targetList) throws Exception;
}
