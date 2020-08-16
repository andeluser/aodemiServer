package shield;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import factory.DaoFactory;

//迫りくる後悔
public class m53 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();

		detailMap.put("playerId", playerId);

		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, enemyPlayerId);

		//フェーズで処理を分ける
		if ("remove".equals(controllDTO.getPhase())) {
			//相手のマナを-2
			int sp = baseDto.getSp();
			sp = sp - 2;

			if (sp < 0) {
				sp = 0;
			}

			baseDto.setSp(sp);
			detailMap.put("SP", sp);

		} else {
			//相手の次のターンの増加SPを２減らす
			int nextSp = baseDto.getTurn_up_sp();

			nextSp = nextSp - 2;

			if (nextSp < 0) {
				nextSp = 0;
			}

			baseDto.setTurn_up_sp(nextSp);
			detailMap.put("nextSP", nextSp);

		}

		//ベースを更新
		baseDao.update(baseDto);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

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
