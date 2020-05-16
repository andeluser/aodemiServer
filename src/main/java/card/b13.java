package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//有翼聖騎士
public class b13 implements CardAbility {

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

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		//自分の情報を取得
		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
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

		HashMap<String, Object> targetMap = new HashMap<String, Object>();
		targetMap.put("list", targetList);
		targetMap.put("playerId", enemyPlayerId);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(targetMap);

		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		ret.put("updateInfo", new HashMap());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		int hp = 0;

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//対象のatkを20上げる
					enemyFieldDto.setPermanent_atk(enemyFieldDto.getPermanent_atk() + 20);
					//アクション終了する
					enemyFieldDto.setAction(1);
					fieldDao.update(enemyFieldDto);

					//自分のＨＰを減らす
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

					hp = fieldDto.getCur_hp();

					int attack = enemyFieldDto.getPermanent_atk() + enemyFieldDto.getCur_atk() + enemyFieldDto.getTurn_atk();

					attack = attack - (fieldDto.getCur_def() + fieldDto.getPermanent_def() + fieldDto.getTurn_def());

					if (attack <= 0) {
						attack = 0;
					}

					hp = hp - attack;

					fieldDto.setCur_hp(hp);

					//Hpが１以下ならクローズ情報を設定
					if (fieldDto.getCur_hp() <= 0) {
						fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
					}
					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("upATK", 20);
					detailMap.put("remove", "actionEnd");
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);

		//自分のHPを相手のＡＴＫ分だけ減らす
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
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
