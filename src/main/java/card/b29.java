package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//斥候
public class b29 implements CardAbility {

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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	//鷹の目
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
				return null;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) {
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

	//最後の追撃
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
		}

		//射程から対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> tmpList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		ArrayList<Object> targetList = new ArrayList<Object>();
		ArrayList<BattleFieldDTO> dtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//行動済のキャラクターは除外する
		for (int i = 0; i < tmpList.size(); i++) {

			int number = Integer.parseInt(tmpList.get(i).toString());

			BattleFieldDTO dto = dtoList.get(number);

			if (!"".equals(dto.getCard_id()) && dto.getAction() == 0 ) {
				targetList.add(number);
			}
		}

		if (targetList.size() == 0) {
			return new HashMap();
		}

		//対象を決定
		int targetFieldNumber = 0;

		if (targetList.size() == 1) {
			targetFieldNumber = (int)targetList.get(0);
		} else {
			Random rand = new Random();
			targetFieldNumber = rand.nextInt(targetList.size());
		}

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, targetFieldNumber);
		int hp = fieldDto.getCur_hp() + fieldDto.getCur_def() + fieldDto.getPermanent_def() + fieldDto.getTurn_def() - 20;
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
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
