package shield;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//神官の祈り
public class b96 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 1) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() == 0) {
			return ret;
		}

		HashMap<String, Object> myTarget = new HashMap<String, Object>();
		myTarget.put("playerId", playerId);
		myTarget.put("list", targetList);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(myTarget);

		HashMap<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		//戻り値設定
		ret.put("updateInfo", new HashMap<String, Object>());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> shieldSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleDeckDAO deckDao = factory.createDeckDAO();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO dto = fieldDao.getAllValue(battleID, player1, list.get(k));
					BattleDeckDTO deckDto = deckDao.getAllValue(battleID, playerId, dto.getDeck_no());

					//対象のユニットを復活させる
					dto.setClose(0);
					dto.setAction(0);
					dto.setClose_number(0);
					dto.setOpen_close_number(0);

					//復活に伴う初期化処理
					if (deckDto.getClose_skill() == 1) {
						dto.setClose_skill(0);
					} else {
						dto.setClose_skill(1);
					}
					dto.setAction(0);
					dto.setPermanent_hp(0);
					dto.setTurn_hp(0);
					dto.setCur_hp(dto.getBase_hp());
					dto.setPermanent_level(0);
					dto.setTurn_level(0);
					dto.setPermanent_atk(0);
					dto.setTurn_atk(0);
					dto.setPermanent_def(0);
					dto.setTurn_def(0);
					dto.setPermanent_speed(0);
					dto.setTurn_speed(0);
					dto.setPermanent_range(0);
					dto.setTurn_range(0);
					dto.setTurn_frm(0);

					fieldDao.update(dto);

					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("remove", "revive");
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

}
