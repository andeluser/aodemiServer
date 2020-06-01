package shield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.StringUtil;

//血魂の短剣
public class m43 implements ShieldAbility {

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

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
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
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		StringUtil util = new StringUtil();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//対象のユニットを消滅
					baseDto.setDisappearance(util.addCardNumberForCamma(baseDto.getDisappearance(), fieldDto.getDeck_no()));
					baseDao.update(baseDto);

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
					detailMap.put("remove", "Vanish");
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);

		//【未行動敵ユニットLV2以上・ランダム1体】返却する
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> enemyFieldList = fieldDao.getAllList(battleID, enemyPlayerId);
		ArrayList enemyTargetList = new ArrayList();

		//一覧から対象を確認する
		for (int i = 0; i < enemyFieldList.size(); i++) {
			BattleFieldDTO dto = enemyFieldList.get(i);

			int level = dto.getPermanent_level() + dto.getTurn_level() + dto.getCur_level();
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getAction() == 0 && level >= 2) {
				enemyTargetList.add(i);
			}
		}

		if (enemyTargetList.size() == 0) {

			updateList.add(updateMap);
			ret.put("updateInfo", updateList);
			ret.put("target", new ArrayList<Object>());

			return ret;
		}

		Random rand = new Random();
		int num = rand.nextInt(enemyTargetList.size());
		int target = (int)enemyTargetList.get(num);

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);

		//対象のユニットを手札に戻す
		BattleDeckDAO deckDao = factory.createDeckDAO();
		BattleDeckDTO deckDto = deckDao.getAllValue(battleID, enemyPlayerId, fieldDto.getDeck_no());
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

		ArrayList<Object> enemyRetList = new ArrayList<Object>();
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		HashMap<String, Object> enemyUpdateMap = new HashMap<String, Object>();

		detailMap.put("playerId", enemyPlayerId);
		detailMap.put("fieldNumber", target);
		detailMap.put("remove", "deck");
		enemyRetList.add(detailMap);

		enemyUpdateMap.put("field", enemyRetList);

		updateList.add(updateMap);
		updateList.add(enemyUpdateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

}
