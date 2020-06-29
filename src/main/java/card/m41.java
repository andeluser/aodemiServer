package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//焔の御使い『フレイメ』
public class m41 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
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

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//対象からランダムで１体選択する
		List result = new ArrayList();
		Collections.shuffle(enemyTargetList);
		result = enemyTargetList.subList(0, 1);

		ArrayList<Object> list = new ArrayList<Object>();

		for (int i = 0; i < result.size(); i++) {

			BattleFieldDTO dto = enemyFieldDtoList.get((int)result.get(i));
			int hp = dto.getCur_hp();
			int def = dto.getPermanent_def() + dto.getTurn_def() + dto.getCur_def();

			int attack = (baseDto.getSpecial_stock() * 5) - def;

			if (attack < 0) {
				attack = 0;
			}

			hp = hp - attack;

			dto.setCur_hp(hp);

			if (hp <= 0) {
				fieldDao.setClose(battleID, dto.getPlayer_id(), dto.getField_no(), dto);
			}
			fieldDao.update(dto);

			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			detailMap.put("playerId", enemyPlayerId);
			detailMap.put("fieldNumber", result.get(i));
			detailMap.put("hp", hp);
			list.add(detailMap);
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", list);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		String enemyPlayerId = playerId;
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//射程から対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> tmpList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		if (tmpList.size() == 0) {
			return new HashMap();
		}

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//対象を決定
		Random rand = new Random();
		int targetFieldNumber = rand.nextInt(tmpList.size());

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, targetFieldNumber);
		int hp = fieldDto.getCur_hp();
		int def = fieldDto.getCur_def() + fieldDto.getPermanent_def() + fieldDto.getTurn_def();

		int attack = (baseDto.getSpecial_stock() * 5) - def;

		if (attack < 0) {
			attack = 0;
		}

		hp = hp - attack;

		if (hp <= 0) {
			fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
		}

		fieldDto.setCur_hp(hp);
		fieldDao.update(fieldDto);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", enemyPlayerId);
		detailMap.put("fieldNumber", targetFieldNumber);
		detailMap.put("hp", hp);

		ArrayList<Object> mylist = new ArrayList<Object>();
		mylist.add(detailMap);

		HashMap<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("field", mylist);

		updateList.add(updateMap);
		updateList.add(myMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
