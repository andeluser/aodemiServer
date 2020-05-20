package shield;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dto.BattleBaseDTO;
import factory.DaoFactory;

//崩御の悲嘆
public class b99 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//自分のＳＰを１減らす
		int sp = baseDto.getSp();
		sp = sp - 1;

		if (sp < 0) {
			sp = 0;
		}

		//ベースを更新
		baseDto.setSp(sp);
		baseDao.update(baseDto);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("SP", sp);
		list.add(detailMap);

		updateMap.put("sp", list);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> shieldSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
