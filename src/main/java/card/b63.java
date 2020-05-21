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

//がっぷり四つ
public class b63 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//宣言ここまで

		//自分のＳＰを１増やす
		int sp = baseDto.getSp();
		sp = sp + 1;
		//ベースを更新
		baseDto.setSp(sp);
		baseDao.update(baseDto);



		//AGI

		//自分
		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO dto = fieldDtoList.get(i);
			//生存しているカードのみ対象に
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
				BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, i);

				int curAGI = 2 - fieldDto.getCur_speed();
				//ターン増加はゼロに
				fieldDto.setTurn_speed(0);
				//永続増加は全体で１０となるように調整
				fieldDto.setPermanent_speed(curAGI);

				//DBを更新
				fieldDao.update(fieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("tupAGI", 0);
				detailMap.put("upAGI", fieldDto.getPermanent_speed());
				retList.add(detailMap);
			}
		}

		//相手
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);


		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO dto = enemyFieldDtoList.get(i);

			//生存しているカードのみ対象に
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
				BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, i);

				int curAGI = 2 - fieldDto.getCur_speed();
				//ターン増加はゼロに
				fieldDto.setTurn_speed(0);
				//永続増加は全体で１０となるように調整
				fieldDto.setPermanent_speed(curAGI);

				//DBを更新
				fieldDao.update(fieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("tupAGI", 0);
				detailMap.put("upAGI", fieldDto.getPermanent_speed());
				retList.add(detailMap);
			}
		}


		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();

		//sp
		detailMap.put("playerId", playerId);
		detailMap.put("SP", sp);
		list.add(detailMap);
		updateMap.put("sp", list);

		//AGI
		updateMap.put("field", retList);


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
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
