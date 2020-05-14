package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import factory.DaoFactory;

//袖の下
public class b3 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//相手のゲージを１０増やす
		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
		int gage = enemyBaseDto.getSpecial_gage() + 10;
		int stock = enemyBaseDto.getSpecial_stock();

		while (gage >= 20) {

			gage = gage - 20;
			stock = stock + 1;

			if (stock >= 5) {
				stock = 5;
			}

		}

		enemyBaseDto.setSpecial_gage(gage);
		enemyBaseDto.setSpecial_stock(stock);
		baseDao.update(enemyBaseDto);

		//自分のゲージを３０増やす
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		gage = baseDto.getSpecial_gage() + 30;
		stock = baseDto.getSpecial_stock();

		while (gage >= 20) {

			gage = gage - 20;
			stock = stock + 1;

			if (stock >= 5) {
				stock = 5;
			}

		}

		baseDto.setSpecial_gage(gage);
		baseDto.setSpecial_stock(stock);

		//SPを２増やす
		baseDto.setSp(baseDto.getSp() + 2);

		baseDao.update(baseDto);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();

		//自分のSP増加
		detailMap.put("playerId", playerId);
		detailMap.put("SP", baseDto.getSp());
		list.add(detailMap);

		updateMap.put("sp", list);

		//相手と自分のゲージが増加
		HashMap<String, Object> detailMapSpecial = new HashMap<String, Object>();
		detailMapSpecial.put("playerId", playerId);
		detailMapSpecial.put("gage", baseDto.getSpecial_gage());
		detailMapSpecial.put("stock", baseDto.getSpecial_stock());

		HashMap<String, Object> detailMapSpecial2 = new HashMap<String, Object>();
		detailMapSpecial2.put("playerId", enemyPlayerId);
		detailMapSpecial2.put("gage", enemyBaseDto.getSpecial_gage());
		detailMapSpecial2.put("stock", enemyBaseDto.getSpecial_stock());

		ArrayList<Object> listSpecial = new ArrayList<Object>();
		listSpecial.add(detailMapSpecial);
		listSpecial.add(detailMapSpecial2);

		updateMap.put("special", listSpecial);

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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
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
