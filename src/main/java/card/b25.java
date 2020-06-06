package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//死兵
public class b25 implements CardAbility {

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
		BattleDeckDAO deckDao = factory.createDeckDAO();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		//自身のカードID
		BattleFieldDTO myFieldDto = fieldDao.getAllList(battleID, playerId).get(fieldNumber);
		String myCard_id = myFieldDto.getCard_id();

		//効果処理
		ArrayList<Object> retList = new ArrayList<Object>();

		ArrayList<BattleDeckDTO> deckDtoList = deckDao.getAllList(battleID, playerId);
		for (int i = 0; i < deckDtoList.size(); i++) {
			BattleDeckDTO deckDto = deckDtoList.get(i);
			//カードIDが同じであれば
			if (myCard_id.equals(deckDto.getCard_id()) && deckDto.getCard_out() == 0) {
				//墓地へ送る
				deckDto.setCard_out(1);
				deckDao.update(deckDto);
				//自身を復活,AGI-1
				int maxHp = myFieldDto.getPermanent_hp() + myFieldDto.getTurn_hp() + myFieldDto.getBase_hp();
				myFieldDto.setCur_hp(maxHp);
				myFieldDto.setPermanent_speed(myFieldDto.getPermanent_speed() - 1);
				fieldDao.update(myFieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", fieldNumber);
				detailMap.put("hp", maxHp);
				detailMap.put("upAGI", myFieldDto.getPermanent_speed());
				retList.add(detailMap);

				break;
			}
		}

		if (retList.size() == 0) {
			return ret;
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
		updateList.add(updateMap);

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
