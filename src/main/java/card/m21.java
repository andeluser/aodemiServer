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
import util.CommonUtil;

//死龍『インヘル』
public class m21 implements CardAbility {

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

		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		if (baseDto.getSpecial_stock() >= 2) {
			return ret;
		}

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//自分のステータスを全て元に戻し、HP＝MHPとする
		fieldDto.setPermanent_atk(0);
		fieldDto.setTurn_atk(0);
		fieldDto.setPermanent_def(0);
		fieldDto.setTurn_def(0);
		fieldDto.setPermanent_hp(0);
		fieldDto.setPermanent_level(0);
		fieldDto.setTurn_level(0);
		fieldDto.setPermanent_range(0);
		fieldDto.setTurn_range(0);
		fieldDto.setPermanent_frm(0);
		fieldDto.setTurn_frm(0);
		fieldDto.setPermanent_speed(0);
		fieldDto.setTurn_speed(0);
		fieldDto.setCur_hp(fieldDto.getBase_hp());
		fieldDto.setClose(0);
		fieldDto.setClose_number(0);
		fieldDto.setOpen_close_number(0);
		fieldDao.update(fieldDto);

		ArrayList<Object> retList = new ArrayList<Object>();
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		detailMap.put("hp", fieldDto.getCur_hp());
		detailMap.put("upLV", fieldDto.getPermanent_level());
		detailMap.put("tupLV", fieldDto.getTurn_level());
		detailMap.put("upHP", fieldDto.getPermanent_hp());
		detailMap.put("upATK", fieldDto.getPermanent_atk());
		detailMap.put("tupATK", fieldDto.getTurn_atk());
		detailMap.put("upDFE", fieldDto.getPermanent_def());
		detailMap.put("tupDFE", fieldDto.getTurn_def());
		detailMap.put("upAGI", fieldDto.getPermanent_speed());
		detailMap.put("tupAGI", fieldDto.getTurn_speed());
		detailMap.put("upRNG", fieldDto.getPermanent_range());
		detailMap.put("tupRNG", fieldDto.getTurn_range());
		detailMap.put("upFRM", fieldDto.getPermanent_frm());
		detailMap.put("tupFRM", fieldDto.getTurn_frm());
		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("field", retList);

		//自分の奥義ゲージを４０増やす
		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList<Object> retList2 = new ArrayList<Object>();

		CommonUtil util = new CommonUtil();

		HashMap<String, Object> map = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), 40, 0);
		baseDto.setSpecial_gage((int)map.get("gage"));
		baseDto.setSpecial_stock((int)map.get("stock"));
		baseDao.update(baseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("gage", baseDto.getSpecial_gage());
		detailMap2.put("stock", baseDto.getSpecial_stock());
		retList2.add(detailMap2);

		updateMap.put("special", retList2);
		updateList.add(updateMap);

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

		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, enemyPlayerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		//相手ユニットに２０ダメージ
		int count = 0;
		//複数クローズの可能性があるので番号を取得する
		int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

		for (int i = 0; i < fieldList.size(); i++) {

			BattleFieldDTO fieldDto = fieldList.get(i);

			if (!"".equals(fieldList.get(i).getCard_id()) && fieldList.get(i).getClose() == 0) {

				int hp = fieldDto.getCur_hp();

				int attack = 20;
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
					count++;
				}

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("hp", hp);
				retList.add(detailMap);
			}
		}

		if (retList.size() == 0) {
			return ret;
		}

		fieldDao.update(fieldList);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("field", retList);

		//自分のゲージを後退数×１０減らす
		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		if (count > 0) {

			HashMap<String, Object> detailMap = new HashMap<String, Object>();
			ArrayList<Object> retList2 = new ArrayList<Object>();

			count = count * -10;

			CommonUtil util = new CommonUtil();

			HashMap<String, Object> map = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), count, 0);
			baseDto.setSpecial_gage((int)map.get("gage"));
			baseDto.setSpecial_stock((int)map.get("stock"));
			baseDao.update(baseDto);

			detailMap2.put("playerId", playerId);
			detailMap2.put("gage", baseDto.getSpecial_gage());
			detailMap2.put("stock", baseDto.getSpecial_stock());
			retList2.add(detailMap2);

			updateMap.put("special", retList2);
		}

		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
