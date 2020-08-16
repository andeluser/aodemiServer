package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//勤勉な処刑人
public class b32 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
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

		//SPを１消費
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		baseDto.setSp(baseDto.getSp() - 1);
		baseDao.update(baseDto);

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
				enemyTargetList.add(i);
			}
		}

		if (enemyTargetList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		//対象からランダムで１体選択する
		List result = new ArrayList();

		ArrayList updateList = new ArrayList();
		Collections.shuffle(enemyTargetList);
		result = enemyTargetList.subList(0, 1);

		ArrayList<Object> list = new ArrayList<Object>();

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		BattleFieldDTO dto = enemyFieldDtoList.get((int)result.get(0));
		int hp = dto.getCur_hp();

		int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();
		attack = attack * 2;

		hp = hp - attack;

		dto.setCur_hp(hp);

		if (hp <= 0) {
			fieldDao.setClose(battleID, dto.getPlayer_id(), dto.getField_no(), dto);
		}
		fieldDao.update(dto);

		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", enemyPlayerId);
		detailMap.put("fieldNumber", result.get(0));
		detailMap.put("hp", hp);
		list.add(detailMap);

		//自分のMHPの半分を受ける
		int maxHp = (fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp()) / 2;
		int myHp = fieldDto.getCur_hp() - maxHp;

		if (myHp <= 0) {
			fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
		}
		fieldDto.setCur_hp(myHp);
		fieldDao.update(fieldDto);

		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();

		detailMap2.put("playerId", playerId);
		detailMap2.put("fieldNumber", fieldNumber);
		detailMap2.put("hp", myHp);

		list.add(detailMap2);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("field", list);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
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

		//対象を計算する
		boolean checkFlg = false;

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 1) {
				checkFlg = true;
				break;
			}
		}

		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 1) {
				checkFlg = true;
				break;
			}
		}


		if (checkFlg) {
			//HPを20回復する
			BattleFieldDTO dto = fieldDtoList.get(fieldNumber);

			int maxHp = dto.getPermanent_hp() + dto.getTurn_hp() + dto.getBase_hp();

			int hp = dto.getCur_hp() + 20;

			if (hp > maxHp) {
				hp = maxHp;
			}
			dto.setCur_hp(hp);
			fieldDao.update(dto);

			//戻り値の作成
			HashMap<String, Object> detailMap = new HashMap<String, Object>();
			HashMap<String, Object> updateMap = new HashMap<String, Object>();
			ArrayList<Object> updateList = new ArrayList<Object>();
			ArrayList<Object> retList = new ArrayList<Object>();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("hp", hp);
			retList.add(detailMap);

			updateMap.put("field", retList);
			updateList.add(updateMap);

			ret.put("updateInfo", updateList);
			ret.put("target", new ArrayList<Object>());
		}

		return ret;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
