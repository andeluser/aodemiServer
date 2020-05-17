package special;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dto.BattleBaseDTO;
import factory.DaoFactory;

//平和の歌
public class o9 implements SpecialAbility {

	@Override
	public HashMap<String, Object> specialSkill(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		//ストックを２消費
		baseDto.setSpecial_stock(baseDto.getSpecial_stock() - 2);

		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		if (baseDto.getBlack() == 0) {
			baseDto.setBlack(1);
			detailMap.put("black", 1);
		}

		if (baseDto.getRed() == 0) {
			baseDto.setRed(1);
			detailMap.put("red", 1);
		}

		if (baseDto.getYellow() == 0) {
			baseDto.setYellow(1);
			detailMap.put("yellow", 1);
		}

		if (baseDto.getBlue() == 0) {
			baseDto.setBlue(1);
			detailMap.put("blue", 1);
		}

		baseDao.update(baseDto);

		if (detailMap.size() == 0) {
			return ret;
		}

		detailMap.put("playerId", playerId);
		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("mana", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> specialSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


}
