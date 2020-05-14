package shield;

import java.util.ArrayList;
import java.util.HashMap;

public interface ShieldAbility {

	HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception;
	HashMap<String, Object> shieldSkillSelect(String battleID, String playerId, ArrayList<Object> targetList) throws Exception;
}
