package special;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//明日への祈り
public class o2 implements SpecialAbility {

	@Override
	public HashMap<String, Object> specialSkill(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, playerId);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストックを２消費
		baseDto.setSpecial_stock(baseDto.getSpecial_stock() - 2);
		baseDao.update(baseDto);

		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < fieldList.size(); i++) {

			if (!"".equals(fieldList.get(i).getCard_id()) && fieldList.get(i).getClose() == 0) {
				int maxHp = fieldList.get(i).getPermanent_hp() + fieldList.get(i).getTurn_hp() + fieldList.get(i).getBase_hp();

				fieldList.get(i).setCur_hp(maxHp);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("hp", maxHp);
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
