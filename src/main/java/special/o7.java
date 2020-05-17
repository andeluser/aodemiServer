package special;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//自由自在
public class o7 implements SpecialAbility {

	@Override
	public HashMap<String, Object> specialSkill(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, playerId);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストックを３消費
		baseDto.setSpecial_stock(baseDto.getSpecial_stock() - 3);
		baseDao.update(baseDto);

		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < fieldList.size(); i++) {

			if (!"".equals(fieldList.get(i).getCard_id()) && fieldList.get(i).getClose() == 0) {
				fieldList.get(i).setTurn_atk(fieldList.get(i).getTurn_atk() + 10);
				fieldList.get(i).setTurn_speed(fieldList.get(i).getTurn_speed() + 1);
				fieldList.get(i).setTurn_range(fieldList.get(i).getTurn_range() + 1);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("tupATK", fieldList.get(i).getTurn_atk());
				detailMap.put("tupAGI", fieldList.get(i).getTurn_speed());
				detailMap.put("tupRNG", fieldList.get(i).getTurn_range());

				retList.add(detailMap);
			}
		}

		if (retList.size() == 0) {
			return ret;
		}

		fieldDao.update(fieldList);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("field", retList);
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
