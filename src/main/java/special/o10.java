package special;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dto.BattleBaseDTO;
import factory.DaoFactory;

//未知への冒険
public class o10 implements SpecialAbility {

	@Override
	public HashMap<String, Object> specialSkill(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		//ストックを４消費
		baseDto.setSpecial_stock(baseDto.getSpecial_stock() - 4);
		//SPを３増やす
		baseDto.setSp(baseDto.getSp() + 3);
		baseDao.update(baseDto);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		detailMap.put("playerId", playerId);
		detailMap.put("SP", baseDto.getSp());
		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("sp", retList);
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
