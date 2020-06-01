package shield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//物質融解
public class m50 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);
		BattleDeckDAO deckDao = factory.createDeckDAO();

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//リムーブフェーズの場合処理しない
		if (!"remove".equals(controllDTO.getPhase())) {
			return ret;
		}

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && list.getAction() == 1) {
				targetList.add(list);
			}
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && list.getAction() == 1) {
				targetList.add(list);
			}
		}

		if (targetList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		//対象からランダムで１体選択する
		Collections.shuffle(targetList);
		List result = targetList.subList(0, 1);

		BattleFieldDTO fieldDto = (BattleFieldDTO)result.get(0);

		//対象のユニットを手札へ戻す
		BattleDeckDTO deckDto = deckDao.getAllValue(battleID, fieldDto.getPlayer_id(), fieldDto.getDeck_no());
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
		ArrayList<Object> retList = new ArrayList<Object>();

		detailMap.put("playerId", fieldDto.getPlayer_id());
		detailMap.put("fieldNumber", fieldDto.getField_no());
		detailMap.put("remove", "deck");
		retList.add(detailMap);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
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
