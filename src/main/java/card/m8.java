package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//邪教の騎士
public class m8 implements CardAbility {

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

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {

			BattleFieldDTO dto = fieldDtoList.get(i);

			//自分以外を選択
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && "黄".equals(dto.getColor())) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() <= 2) {

			return ret;
		}

		HashMap<String, Object> updateMap = new HashMap<String, Object>();

		//ターン中ATKを１０増やす
		fieldDto.setTurn_atk(fieldDto.getTurn_atk() + 10);
		fieldDao.update(fieldDto);

		//奥義ゲージを１０増やす
		int gage = baseDto.getSpecial_gage() + 10;

		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList<Object> retList2 = new ArrayList<Object>();

		if (gage >= 20) {
			gage = gage - 20;
			baseDto.setSpecial_stock(baseDto.getSpecial_stock() + 1);

			if (baseDto.getSpecial_stock() > 5) {
				baseDto.setSpecial_stock(5);
			}

			detailMap2.put("stock", baseDto.getSpecial_stock());

		}

		baseDto.setSpecial_gage(gage);
		baseDao.update(baseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("gage", gage);
		retList2.add(detailMap2);
		updateMap.put("special", retList2);

		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupATK", fieldDto.getTurn_atk());
		retList.add(detailMap);
		updateMap.put("field", retList);

		updateList.add(updateMap);

		//戻り値設定
		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		//SPを２消費
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		baseDto.setSp(baseDto.getSp() - 2);
		baseDao.update(baseDto);

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

		//対象なし
		if (tmpList.size() == 0) {
			return ret;
		}

		Random rand = new Random();
		int num = rand.nextInt(tmpList.size());

		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, enemyPlayerId);
		BattleFieldDTO myDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//どの列かを判定し処理する

		int target = (int)tmpList.get(num);

		int startRowNumber = 0;

		if (target <= 2) {
			startRowNumber = 0;
		} else if (target > 2 && target <= 5) {
			startRowNumber = 3;
		} else if (target > 5 && target <= 8) {
			startRowNumber = 6;
		}

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//複数クローズの可能性があるので番号を取得する
		int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

		for (int i = 0; i < 3; i++) {
			int index  = startRowNumber + i;
			BattleFieldDTO fieldDto = fieldList.get(index);

			if (fieldDto.getCard_id() != null && !"".equals(fieldDto.getCard_id()) && fieldDto.getClose() == 0) {

				//対象にダメージ
				int hp = fieldDto.getCur_hp();

				int attack = myDto.getPermanent_atk() + myDto.getTurn_atk() + myDto.getCur_atk();
				attack = attack - fieldDto.getPermanent_def() - fieldDto.getTurn_def() - fieldDto.getCur_def();

				if (attack <= 0) {
					attack = 0;
				}

				hp = hp - attack;

				fieldDto.setCur_hp(hp);

				//Hpが１以下ならクローズ情報を設定
				if (fieldDto.getCur_hp() <= 0) {
					fieldDto.setClose(1);
					fieldDto.setClose_number(closeNumber);
				}

				fieldDao.update(fieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", index);
				detailMap.put("hp", hp);
				retList.add(detailMap);
			}
		}

		updateMap.put("field", retList);
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
