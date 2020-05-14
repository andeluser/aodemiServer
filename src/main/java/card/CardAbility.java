package card;

import java.util.ArrayList;
import java.util.HashMap;

public interface CardAbility {

	HashMap<String, Object> open(String battleID, String playerId) throws Exception;
	HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList) throws Exception;
	HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception;
	HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception;
	HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception;
	HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception;
	HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception;
	HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception;
	HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception;
	HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception;
	HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception;
	HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception;
}
