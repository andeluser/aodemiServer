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
import util.StringUtil;

//影の刃
public class b27 implements CardAbility {

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
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//自分の情報を取得
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);
		ArrayList<Object> orderList = new ArrayList<Object>();

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//ダメージ量を計算
			int attack = 100;

			//攻撃対象のHPを減らす
			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);

			int def = fieldDto.getPermanent_def() + fieldDto.getTurn_def() + fieldDto.getCur_def();

			attack = attack - def;

			if (attack < 0) {
				attack = 0;
			}

			//HP(防御は無視)
			int enemyHp = enemyFieldDto.getCur_hp();

			//HPを計算
			enemyHp = enemyHp - attack;

			//相手のHPを設定
			enemyFieldDto.setCur_hp(enemyHp);

			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
			if (enemyHp <= 0) {
				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
			}

			fieldDao.update(enemyFieldDto);

			//戻り値を設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("playerId", enemyPlayerId);
			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
			updateList.add(updateMap);

			HashMap<String, Object> orderMap = new HashMap<String, Object>();

			orderMap.put("field", updateList);
			orderList.add(orderMap);

			ret.put("updateInfo", orderList);

		}

		//自分を墓地へ送る
		StringUtil util = new StringUtil();
		baseDto.setCemetery(util.addCardNumberForCamma(baseDto.getCemetery(), fieldDto.getDeck_no()));
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
		fieldDto.setPermanent_frm(0);
		fieldDto.setTurn_frm(0);
		fieldDto.setCur_frm(0);
		fieldDto.setDeck_no(0);

		fieldDao.update(fieldDto);

		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();
		HashMap<String, Object> orderMap2 = new HashMap<String, Object>();

		detailMap.put("playerId", fieldDto.getPlayer_id());
		detailMap.put("fieldNumber", fieldDto.getField_no());
		detailMap.put("remove", "cemetery");
		retList.add(detailMap);

		orderMap2.put("field", retList);
		orderList.add(orderMap2);

		ret.put("updateInfo", orderList);

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
