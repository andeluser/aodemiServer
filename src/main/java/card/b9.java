package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//神罰
public class b9 implements CardAbility {

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

		ArrayList<Object> retTargetList = new ArrayList<Object>();

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
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

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
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

		//更新は墓地送還のみ
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
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

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

					//対象のユニットに自分の奥義ストック×１０を与える
					//ダメージ量を計算
					int atack =baseDto.getSpecial_stock() * 10;

					//HPに防御を考慮
					int enemyHp = fieldDto.getCur_hp() + fieldDto.getPermanent_def() + fieldDto.getTurn_def() + fieldDto.getCur_def();

					//HPを計算
					enemyHp = enemyHp - atack;

					//相手のHPを設定
					fieldDto.setCur_hp(enemyHp);

					//相手のHPがゼロ以下となった場合はクローズ判定を立てる
					if (enemyHp <= 0) {
						fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
					}

					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("hp", enemyHp);
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

	//タダでは帰らない
	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		//相手の情報を決定
		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		ArrayList<Object> targetList = new ArrayList<Object>();
		ArrayList<BattleFieldDTO> dtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象の一覧を取得
		for (int i = 0; i < dtoList.size(); i++) {

			BattleFieldDTO dto = dtoList.get(i);

			if (!"".equals(dto.getCard_id()) && dto.getClose() == 0 ) {
				targetList.add(i);
			}
		}

		if (targetList.size() == 0) {
			return new HashMap();
		}

		//戻り値設定
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
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//対象のatkを10下げる
					int atk = enemyFieldDto.getPermanent_atk() - 10;

					enemyFieldDto.setPermanent_atk(atk);
					fieldDao.update(enemyFieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("upATK", atk);
					retList.add(detailMap);
				}
			}
		}

		//戻り値設定
		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
