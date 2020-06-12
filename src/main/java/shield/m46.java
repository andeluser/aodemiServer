package shield;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//熾天使の羽ばたき
public class m46 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		if (baseDto.getSp() < 1) {
			return ret;
		}

		//SPを1減らす
		baseDto.setSp(baseDto.getSp() - 1);
		baseDao.update(baseDto);

		//リムーブフェーズ以外、あるいはライフが６以上なら処理しない
		if (!"remove".equals(controllDTO.getPhase()) || baseDto.getLife() >= 6) {
			return ret;
		}

		ArrayList<Object> retTargetList = new ArrayList<Object>();

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			int agi = list.getPermanent_level() + list.getTurn_level() + list.getCur_level();
			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && agi <= 3) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() != 0) {
			HashMap<String, Object> myTarget = new HashMap<String, Object>();
			myTarget.put("playerId", playerId);
			myTarget.put("list", targetList);
			retTargetList.add(myTarget);
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			int agi = list.getPermanent_level() + list.getTurn_level() + list.getCur_level();
			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && agi <= 3) {
				enemyTargetList.add(enemyFieldDtoList.get(i).getField_no());
			}
		}

		if (enemyTargetList.size() != 0) {
			HashMap<String, Object> enemyTarget = new HashMap<String, Object>();
			enemyTarget.put("playerId", enemyPlayerId);
			enemyTarget.put("list", enemyTargetList);
			retTargetList.add(enemyTarget);
		}

		if (targetList.size() == 0 && enemyTargetList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

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

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		BattleDeckDAO deckDao = factory.createDeckDAO();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//対象のユニットをデッキへ戻す
					BattleDeckDTO deckDto = deckDao.getAllValue(battleID, player1, fieldDto.getDeck_no());
					deckDto.setCard_out(0);
					deckDao.update(deckDto);

					//盤面から削除する
					fieldDto.setCard_id("");
					fieldDto.setClose(0);
					fieldDto.setClose_number(0);
					fieldDto.setOpen_close_number(0);
					fieldDto.setClose_skill(0);
					fieldDto.setStart_action(0);
					fieldDto.setAction(0);
					fieldDto.setColor("");
					fieldDto.setType1("");
					fieldDto.setType2("");
					fieldDto.setPermanent_hp(0);
					fieldDto.setTurn_hp(0);
					fieldDto.setBase_hp(0);
					fieldDto.setCur_hp(0);
					fieldDto.setPermanent_level(0);
					fieldDto.setTurn_level(0);
					fieldDto.setCur_level(0);
					fieldDto.setPermanent_atk(0);
					fieldDto.setTurn_atk(0);
					fieldDto.setCur_atk(0);
					fieldDto.setPermanent_def(0);
					fieldDto.setTurn_def(0);
					fieldDto.setCur_def(0);
					fieldDto.setPermanent_speed(0);
					fieldDto.setTurn_speed(0);
					fieldDto.setCur_speed(0);
					fieldDto.setPermanent_range(0);
					fieldDto.setTurn_range(0);
					fieldDto.setCur_range(0);
					fieldDto.setDeck_no(0);

					fieldDao.update(fieldDto);

					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("remove", "deck");
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
