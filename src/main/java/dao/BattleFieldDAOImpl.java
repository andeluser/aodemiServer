package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.AodemiLogger;
import util.DBManeger;

public class BattleFieldDAOImpl implements BattleFieldDAO {

	public BattleFieldDTO getAllValue(String battleID, String playerId, int fieldNumber) throws Exception {

		BattleFieldDTO ret = new BattleFieldDTO();
		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "' AND FIELD_NO = '" + fieldNumber + "'";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		ret.setBattle_id(rs.getString("BATTLE_ID"));
	    		ret.setPlayer_id(rs.getString("PLAYER_ID"));
	    		ret.setField_no(rs.getInt("FIELD_NO"));
	    		ret.setSub_no(rs.getInt("SUB_NO"));
	    		ret.setNew_flg(rs.getInt("NEW_FLG"));
	    		ret.setCard_id(rs.getString("CARD_ID"));
	    		ret.setClose(rs.getInt("CLOSE"));
	    		ret.setClose_number(rs.getInt("CLOSE_NUMBER"));
	    		ret.setOpen_close_number((rs.getInt("OPEN_CLOSE_NUMBER")));
	    		ret.setClose_skill(rs.getInt("CLOSE_SKILL"));
	    		ret.setStart_action(rs.getInt("START_ACTION"));
	    		ret.setAction(rs.getInt("ACTION"));
	    		ret.setColor(rs.getString("COLOR"));
	    		ret.setType1(rs.getString("TYPE1"));
	    		ret.setType2(rs.getString("TYPE2"));
	    		ret.setPermanent_hp(rs.getInt("PERMANENT_HP"));
	    		ret.setTurn_hp(rs.getInt("TURN_HP"));
	    		ret.setBase_hp(rs.getInt("BASE_HP"));
	    		ret.setCur_hp(rs.getInt("CUR_HP"));
	    		ret.setPermanent_level(rs.getInt("PERMANENT_LEVEL"));
	    		ret.setTurn_level(rs.getInt("TURN_LEVEL"));
	    		ret.setCur_level(rs.getInt("CUR_LEVEL"));
	    		ret.setPermanent_atk(rs.getInt("PERMANENT_ATK"));
	    		ret.setTurn_atk(rs.getInt("TURN_ATK"));
	    		ret.setCur_atk(rs.getInt("CUR_ATK"));
	    		ret.setPermanent_def(rs.getInt("PERMANENT_DEF"));
	    		ret.setTurn_def(rs.getInt("TURN_DEF"));
	    		ret.setCur_def(rs.getInt("CUR_DEF"));
	    		ret.setPermanent_speed(rs.getInt("PERMANENT_SPEED"));
	    		ret.setTurn_speed(rs.getInt("TURN_SPEED"));
	    		ret.setCur_speed(rs.getInt("CUR_SPEED"));
	    		ret.setPermanent_range(rs.getInt("PERMANENT_RANGE"));
	    		ret.setTurn_range(rs.getInt("TURN_RANGE"));
	    		ret.setCur_range(rs.getInt("CUR_RANGE"));
	    		ret.setDeck_no(rs.getInt("DECK_NO"));
	    		ret.setUpdate_date(rs.getTimestamp("UPDATE_DATE"));
	    	}

			return ret;

    	} finally {
    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	public ArrayList<BattleFieldDTO> getAllList(String battleID, String playerId) throws Exception {

		ArrayList<BattleFieldDTO> list = new ArrayList<BattleFieldDTO>();

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "' ORDER BY FIELD_NO";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		BattleFieldDTO dto = new BattleFieldDTO();

	    		dto.setBattle_id(rs.getString("BATTLE_ID"));
	    		dto.setPlayer_id(rs.getString("PLAYER_ID"));
	    		dto.setField_no(rs.getInt("FIELD_NO"));
	    		dto.setSub_no(rs.getInt("SUB_NO"));
	    		dto.setNew_flg(rs.getInt("NEW_FLG"));
	    		dto.setCard_id(rs.getString("CARD_ID"));
	    		dto.setClose(rs.getInt("CLOSE"));
	    		dto.setClose_number(rs.getInt("CLOSE_NUMBER"));
	    		dto.setOpen_close_number((rs.getInt("OPEN_CLOSE_NUMBER")));
	    		dto.setClose_skill(rs.getInt("CLOSE_SKILL"));
	    		dto.setStart_action(rs.getInt("START_ACTION"));
	    		dto.setAction(rs.getInt("ACTION"));
	    		dto.setColor(rs.getString("COLOR"));
	    		dto.setType1(rs.getString("TYPE1"));
	    		dto.setType2(rs.getString("TYPE2"));
	    		dto.setPermanent_hp(rs.getInt("PERMANENT_HP"));
	    		dto.setTurn_hp(rs.getInt("TURN_HP"));
	    		dto.setBase_hp(rs.getInt("BASE_HP"));
	    		dto.setCur_hp(rs.getInt("CUR_HP"));
	    		dto.setPermanent_level(rs.getInt("PERMANENT_LEVEL"));
	    		dto.setTurn_level(rs.getInt("TURN_LEVEL"));
	    		dto.setCur_level(rs.getInt("CUR_LEVEL"));
	    		dto.setPermanent_atk(rs.getInt("PERMANENT_ATK"));
	    		dto.setTurn_atk(rs.getInt("TURN_ATK"));
	    		dto.setCur_atk(rs.getInt("CUR_ATK"));
	    		dto.setPermanent_def(rs.getInt("PERMANENT_DEF"));
	    		dto.setTurn_def(rs.getInt("TURN_DEF"));
	    		dto.setCur_def(rs.getInt("CUR_DEF"));
	    		dto.setPermanent_speed(rs.getInt("PERMANENT_SPEED"));
	    		dto.setTurn_speed(rs.getInt("TURN_SPEED"));
	    		dto.setCur_speed(rs.getInt("CUR_SPEED"));
	    		dto.setPermanent_range(rs.getInt("PERMANENT_RANGE"));
	    		dto.setTurn_range(rs.getInt("TURN_RANGE"));
	    		dto.setCur_range(rs.getInt("CUR_RANGE"));
	    		dto.setDeck_no(rs.getInt("DECK_NO"));
	    		dto.setUpdate_date(rs.getTimestamp("UPDATE_DATE"));

	    		list.add(dto);
	    	}

			return list;

    	} finally {
    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	public int insert(BattleFieldDTO dto) throws Exception {

		int ret = 0;

    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "INSERT INTO BATTLE_FIELD VALUES ("
					+ "'" + dto.getBattle_id() + "'"
					+ ", '" + dto.getPlayer_id() + "'"
					+ ", '" + dto.getField_no() + "'"
					+ ", '" + dto.getSub_no() + "'"
					+ ", '" + dto.getNew_flg() + "'"
					+ ", '" + dto.getCard_id() + "'"
					+ ", '" + dto.getClose() + "'"
					+ ", '" + dto.getClose_number() + "'"
					+ ", '" + dto.getOpen_close_number() + "'"
					+ ", '" + dto.getClose_skill() + "'"
					+ ", '" + dto.getStart_action() + "'"
					+ ", '" + dto.getAction() + "'"
					+ ", '" + dto.getColor() + "'"
					+ ", '" + dto.getType1() + "'"
					+ ", '" + dto.getType2() + "'"
					+ ", '" + dto.getPermanent_hp() + "'"
					+ ", '" + dto.getTurn_hp() + "'"
					+ ", '" + dto.getBase_hp() + "'"
					+ ", '" + dto.getCur_hp() + "'"
					+ ", '" + dto.getPermanent_level() + "'"
					+ ", '" + dto.getTurn_level() + "'"
					+ ", '" + dto.getCur_level() + "'"
					+ ", '" + dto.getPermanent_atk() + "'"
					+ ", '" + dto.getTurn_atk() + "'"
					+ ", '" + dto.getCur_atk() + "'"
					+ ", '" + dto.getPermanent_def() + "'"
					+ ", '" + dto.getTurn_def() + "'"
					+ ", '" + dto.getCur_def() + "'"
					+ ", '" + dto.getPermanent_speed() + "'"
					+ ", '" + dto.getTurn_speed() + "'"
					+ ", '" + dto.getCur_speed() + "'"
					+ ", '" + dto.getPermanent_range() + "'"
					+ ", '" + dto.getTurn_range() + "'"
					+ ", '" + dto.getCur_range() + "'"
					+ ", '" + dto.getDeck_no() + "'"
					+ ", CURRENT_TIMESTAMP)";

			ret = stmt.executeUpdate(sql);

			conn.commit();

    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

    	return ret;
	}

	public int deleteBattle(BattleFieldDTO dto) throws Exception {

		int ret = 0;
    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "DELETE FROM BATTLE_FIELD WHERE PLAYER_ID = '" + dto.getPlayer_id() + "'";

			ret = stmt.executeUpdate(sql);

			conn.commit();

    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

    	return ret;
	}

	public int update(BattleFieldDTO dto) throws Exception {

		int ret = 0;
		DBManeger accesser = new DBManeger();
		Connection conn = null;
    	Statement stmt = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "UPDATE BATTLE_FIELD SET "
//					+ ", PLAYER_ID = '" + dto.getPlayer_id() + "'"
//					+ ", FIELD_NO = '" + dto.getField_no() + "'"
//					+ ", SUB_NO = '" + dto.getSub_no() + "'"
//					+ ", NEW_FLG = '" + dto.getNew_flg() + "'"
					+ "CARD_ID = '" + dto.getCard_id() + "'"
					+ ", CLOSE = '" + dto.getClose() + "'"
					+ ", CLOSE_NUMBER = '" + dto.getClose_number() + "'"
					+ ", OPEN_CLOSE_NUMBER = '" + dto.getOpen_close_number() + "'"
					+ ", CLOSE_SKILL = '" + dto.getClose_skill() + "'"
					+ ", START_ACTION = '" + dto.getStart_action() + "'"
					+ ", ACTION = '" + dto.getAction() + "'"
					+ ", COLOR = '" + dto.getColor() + "'"
					+ ", TYPE1 = '" + dto.getType1() + "'"
					+ ", TYPE2 = '" + dto.getType2() + "'"
					+ ", PERMANENT_HP = '" + dto.getPermanent_hp() + "'"
					+ ", TURN_HP = '" + dto.getTurn_hp() + "'"
					+ ", BASE_HP = '" + dto.getBase_hp() + "'"
					+ ", CUR_HP = '" + dto.getCur_hp() + "'"
					+ ", PERMANENT_LEVEL = '" + dto.getPermanent_level() + "'"
					+ ", TURN_LEVEL = '" + dto.getTurn_level() + "'"
					+ ", CUR_LEVEL = '" + dto.getCur_level() + "'"
					+ ", PERMANENT_ATK = '" + dto.getPermanent_atk() + "'"
					+ ", TURN_ATK = '" + dto.getTurn_atk() + "'"
					+ ", CUR_ATK = '" + dto.getCur_atk() + "'"
					+ ", PERMANENT_DEF = '" + dto.getPermanent_def() + "'"
					+ ", TURN_DEF = '" + dto.getTurn_def() + "'"
					+ ", CUR_DEF = '" + dto.getCur_def() + "'"
					+ ", PERMANENT_SPEED = '" + dto.getPermanent_speed() + "'"
					+ ", TURN_SPEED = '" + dto.getTurn_speed() + "'"
					+ ", CUR_SPEED = '" + dto.getCur_speed() + "'"
					+ ", PERMANENT_RANGE = '" + dto.getPermanent_range() + "'"
					+ ", TURN_RANGE = '" + dto.getTurn_range() + "'"
					+ ", CUR_RANGE = '" + dto.getCur_range() + "'"
					+ ", DECK_NO = '" + dto.getDeck_no() + "'"
					+ ", UPDATE_DATE = CURRENT_TIMESTAMP"
					+ " WHERE BATTLE_ID = '" + dto.getBattle_id() + "' AND PLAYER_ID = '" + dto.getPlayer_id()+ "' AND FIELD_NO ='" + dto.getField_no() + "'";

			AodemiLogger.writeLogOnly(sql);

	    	ret = stmt.executeUpdate(sql);
	     	conn.commit();

    	} finally {

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

    	return ret;
	}

	public void update(ArrayList<BattleFieldDTO> list) throws Exception {

		DBManeger accesser = new DBManeger();
		Connection conn = null;
    	Statement stmt = null;

    	try {

    		conn = accesser.getConnection();
    		conn.setAutoCommit(false);

    		for (int i =0; i < list.size(); i++) {

    	    	stmt = conn.createStatement();

    	    	BattleFieldDTO dto = list.get(i);

    			String sql = "UPDATE BATTLE_FIELD SET "
    					+ "CARD_ID = '" + dto.getCard_id() + "'"
    					+ ", CLOSE = '" + dto.getClose() + "'"
    					+ ", CLOSE_NUMBER = '" + dto.getClose_number() + "'"
    					+ ", OPEN_CLOSE_NUMBER = '" + dto.getOpen_close_number() + "'"
    					+ ", CLOSE_SKILL = '" + dto.getClose_skill() + "'"
    					+ ", START_ACTION = '" + dto.getStart_action() + "'"
    					+ ", ACTION = '" + dto.getAction() + "'"
						+ ", COLOR = '" + dto.getColor() + "'"
						+ ", TYPE1 = '" + dto.getType1() + "'"
						+ ", TYPE2 = '" + dto.getType2() + "'"
    					+ ", PERMANENT_HP = '" + dto.getPermanent_hp() + "'"
    					+ ", TURN_HP = '" + dto.getTurn_hp() + "'"
    					+ ", BASE_HP = '" + dto.getBase_hp() + "'"
    					+ ", CUR_HP = '" + dto.getCur_hp() + "'"
    					+ ", PERMANENT_LEVEL = '" + dto.getPermanent_level() + "'"
    					+ ", TURN_LEVEL = '" + dto.getTurn_level() + "'"
    					+ ", CUR_LEVEL = '" + dto.getCur_level() + "'"
    					+ ", PERMANENT_ATK = '" + dto.getPermanent_atk() + "'"
    					+ ", TURN_ATK = '" + dto.getTurn_atk() + "'"
    					+ ", CUR_ATK = '" + dto.getCur_atk() + "'"
    					+ ", PERMANENT_DEF = '" + dto.getPermanent_def() + "'"
    					+ ", TURN_DEF = '" + dto.getTurn_def() + "'"
    					+ ", CUR_DEF = '" + dto.getCur_def() + "'"
    					+ ", PERMANENT_SPEED = '" + dto.getPermanent_speed() + "'"
    					+ ", TURN_SPEED = '" + dto.getTurn_speed() + "'"
    					+ ", CUR_SPEED = '" + dto.getCur_speed() + "'"
    					+ ", PERMANENT_RANGE = '" + dto.getPermanent_range() + "'"
    					+ ", TURN_RANGE = '" + dto.getTurn_range() + "'"
    					+ ", CUR_RANGE = '" + dto.getCur_range() + "'"
    					+ ", DECK_NO = '" + dto.getDeck_no() + "'"
    					+ ", UPDATE_DATE = CURRENT_TIMESTAMP"
    					+ " WHERE BATTLE_ID = '" + dto.getBattle_id() + "' AND PLAYER_ID = '" + dto.getPlayer_id()+ "' AND FIELD_NO ='" + dto.getField_no() + "'";

    			AodemiLogger.writeLogOnly(sql);

    	    	stmt.executeUpdate(sql);

    		}

	     	conn.commit();

    	} finally {

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}
	}

	//次のリムーブ対象を取得
	public BattleFieldDTO getNextClose(String battleID) throws Exception {

		ArrayList<BattleFieldDTO> list = new ArrayList<BattleFieldDTO>();
		BattleFieldDTO ret = new BattleFieldDTO();

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "'"
					+ " AND CLOSE_NUMBER = "
					+ "(SELECT MIN(CLOSE_NUMBER) AS CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND CLOSE_NUMBER > 0"
					+ " AND CARD_ID IS NOT NULL)"
					+ " AND CARD_ID IS NOT NULL ";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		BattleFieldDTO dto = new BattleFieldDTO();

	    		dto.setBattle_id(rs.getString("BATTLE_ID"));
	    		dto.setPlayer_id(rs.getString("PLAYER_ID"));
	    		dto.setField_no(rs.getInt("FIELD_NO"));
	    		dto.setSub_no(rs.getInt("SUB_NO"));
	    		dto.setNew_flg(rs.getInt("NEW_FLG"));
	    		dto.setCard_id(rs.getString("CARD_ID"));
	    		dto.setClose(rs.getInt("CLOSE"));
	    		dto.setClose_number(rs.getInt("CLOSE_NUMBER"));
	    		dto.setOpen_close_number(rs.getInt("OPEN_CLOSE_NUMBER"));
	    		dto.setClose_skill(rs.getInt("CLOSE_SKILL"));
	    		dto.setStart_action(rs.getInt("START_ACTION"));
	    		dto.setAction(rs.getInt("ACTION"));
	    		dto.setColor(rs.getString("COLOR"));
	    		dto.setType1(rs.getString("TYPE1"));
	    		dto.setType2(rs.getString("TYPE2"));
	    		dto.setPermanent_hp(rs.getInt("PERMANENT_HP"));
	    		dto.setTurn_hp(rs.getInt("TURN_HP"));
	    		dto.setBase_hp(rs.getInt("BASE_HP"));
	    		dto.setCur_hp(rs.getInt("CUR_HP"));
	    		dto.setPermanent_level(rs.getInt("PERMANENT_LEVEL"));
	    		dto.setTurn_level(rs.getInt("TURN_LEVEL"));
	    		dto.setCur_level(rs.getInt("CUR_LEVEL"));
	    		dto.setPermanent_atk(rs.getInt("PERMANENT_ATK"));
	    		dto.setTurn_atk(rs.getInt("TURN_ATK"));
	    		dto.setCur_atk(rs.getInt("CUR_ATK"));
	    		dto.setPermanent_def(rs.getInt("PERMANENT_DEF"));
	    		dto.setTurn_def(rs.getInt("TURN_DEF"));
	    		dto.setCur_def(rs.getInt("CUR_DEF"));
	    		dto.setPermanent_speed(rs.getInt("PERMANENT_SPEED"));
	    		dto.setTurn_speed(rs.getInt("TURN_SPEED"));
	    		dto.setCur_speed(rs.getInt("CUR_SPEED"));
	    		dto.setPermanent_range(rs.getInt("PERMANENT_RANGE"));
	    		dto.setTurn_range(rs.getInt("TURN_RANGE"));
	    		dto.setCur_range(rs.getInt("CUR_RANGE"));
	    		dto.setDeck_no(rs.getInt("DECK_NO"));
	    		dto.setUpdate_date(rs.getTimestamp("UPDATE_DATE"));

	    		list.add(dto);
	    	}

	    	ret = getCloseDto(list);

			return ret;

    	} finally {
    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	//次のリムーブ対象を取得
	public void checkOpenClose(String battleID) throws Exception {

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	Statement stmt2 = null;
    	Statement stmt3 = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "'"
					+ " AND CLOSE_NUMBER = "
					+ "(SELECT MIN(CLOSE_NUMBER) AS CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND CLOSE_NUMBER > 0"
					+ " AND CARD_ID IS NOT NULL)"
					+ " AND CARD_ID IS NOT NULL ";

	    	rs = stmt.executeQuery(sql);

	    	if (rs.next()) {
	    		//対象レコードが居れば何もしない
	    	} else {
	    		//クローズ対象が無くなった場合、オープンスキルでクローズした対象を通常のクローズ順に差し替える
		    	stmt2 = conn.createStatement();
				String sql2 = "UPDATE BATTLE_FIELD SET CLOSE_NUMBER = OPEN_CLOSE_NUMBER WHERE BATTLE_ID = '" + battleID + "'" + " AND CLOSE = 1";

		    	stmt2.executeUpdate(sql2);

		    	if (stmt2 != null) {
	    			stmt2.close();
	    		}

		    	//同時にオープンクローズ順を初期化する
		    	stmt3 = conn.createStatement();
				String sql3 = "UPDATE BATTLE_FIELD SET OPEN_CLOSE_NUMBER ='0' WHERE BATTLE_ID = '" + battleID + "'" + " AND CLOSE = 1";

		    	stmt3.executeUpdate(sql3);

		    	if (stmt3 != null) {
	    			stmt3.close();
	    		}

		    	conn.commit();
	    	}

	    	if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}


    	} finally {

    		if (stmt3 != null) {
    			stmt3.close();
    		}

    		if (stmt2 != null) {
    			stmt2.close();
    		}

    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	//次のクローズスキル対象を取得
	public BattleFieldDTO getNextCloseSkill(String battleID) throws Exception {

		ArrayList<BattleFieldDTO> list = new ArrayList<BattleFieldDTO>();
		BattleFieldDTO ret = new BattleFieldDTO();

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "'"
					+ " AND CLOSE_NUMBER = "
					+ "(SELECT MIN(CLOSE_NUMBER) AS CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND CLOSE_NUMBER > 0"
					+ " AND CARD_ID IS NOT NULL "
					+ " AND CLOSE_SKILL = 0)"
					+ " AND CARD_ID IS NOT NULL "
					+ " AND CLOSE_SKILL = 0";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		BattleFieldDTO dto = new BattleFieldDTO();

	    		dto.setBattle_id(rs.getString("BATTLE_ID"));
	    		dto.setPlayer_id(rs.getString("PLAYER_ID"));
	    		dto.setField_no(rs.getInt("FIELD_NO"));
	    		dto.setSub_no(rs.getInt("SUB_NO"));
	    		dto.setNew_flg(rs.getInt("NEW_FLG"));
	    		dto.setCard_id(rs.getString("CARD_ID"));
	    		dto.setClose(rs.getInt("CLOSE"));
	    		dto.setClose_number(rs.getInt("CLOSE_NUMBER"));
	    		dto.setOpen_close_number(rs.getInt("OPEN_CLOSE_NUMBER"));
	    		dto.setClose_skill(rs.getInt("CLOSE_SKILL"));
	    		dto.setStart_action(rs.getInt("START_ACTION"));
	    		dto.setAction(rs.getInt("ACTION"));
	    		dto.setColor(rs.getString("COLOR"));
	    		dto.setType1(rs.getString("TYPE1"));
	    		dto.setType2(rs.getString("TYPE2"));
	    		dto.setPermanent_hp(rs.getInt("PERMANENT_HP"));
	    		dto.setTurn_hp(rs.getInt("TURN_HP"));
	    		dto.setBase_hp(rs.getInt("BASE_HP"));
	    		dto.setCur_hp(rs.getInt("CUR_HP"));
	    		dto.setPermanent_level(rs.getInt("PERMANENT_LEVEL"));
	    		dto.setTurn_level(rs.getInt("TURN_LEVEL"));
	    		dto.setCur_level(rs.getInt("CUR_LEVEL"));
	    		dto.setPermanent_atk(rs.getInt("PERMANENT_ATK"));
	    		dto.setTurn_atk(rs.getInt("TURN_ATK"));
	    		dto.setCur_atk(rs.getInt("CUR_ATK"));
	    		dto.setPermanent_def(rs.getInt("PERMANENT_DEF"));
	    		dto.setTurn_def(rs.getInt("TURN_DEF"));
	    		dto.setCur_def(rs.getInt("CUR_DEF"));
	    		dto.setPermanent_speed(rs.getInt("PERMANENT_SPEED"));
	    		dto.setTurn_speed(rs.getInt("TURN_SPEED"));
	    		dto.setCur_speed(rs.getInt("CUR_SPEED"));
	    		dto.setPermanent_range(rs.getInt("PERMANENT_RANGE"));
	    		dto.setTurn_range(rs.getInt("TURN_RANGE"));
	    		dto.setCur_range(rs.getInt("CUR_RANGE"));
	    		dto.setDeck_no(rs.getInt("DECK_NO"));
	    		dto.setUpdate_date(rs.getTimestamp("UPDATE_DATE"));

	    		list.add(dto);
	    	}

	    	ret = getCloseDto(list);

			return ret;

    	} finally {
    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	//クローズの情報を設定する（データの更新はなし）
	public void setClose(String battleID, String playerId, int fieldNumber, BattleFieldDTO fieldDto) throws Exception {

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	Statement stmt2 = null;
    	ResultSet rs2 = null;

    	DaoFactory factory = new DaoFactory();
    	BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

    	try {

    		conn = accesser.getConnection();

    		//セット、オープン、リムーブフェーズの場合は順序を
    		if ("set".equals(controllDto.getPhase()) || "open".equals(controllDto.getPhase()) || "remove".equals(controllDto.getPhase())) {

    		} else {
    			//
    		}

	    	stmt = conn.createStatement();
			String sql = "SELECT CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "'"
					+ " AND CLOSE_NUMBER = "
					+ "(SELECT MIN(CLOSE_NUMBER) AS CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND CLOSE_NUMBER > 0"
					+ " AND CARD_ID IS NOT NULL) "
					+ " AND CARD_ID IS NOT NULL ";

	    	rs = stmt.executeQuery(sql);

	    	int closeNumber = 0;
	    	//最小のクローズ順を取得
	    	if (rs.next()) {
	    		closeNumber = rs.getInt("CLOSE_NUMBER");
	    	}

	    	//クローズを設定する
	    	fieldDto.setClose(1);

	    	if ("set".equals(controllDto.getPhase()) || "open".equals(controllDto.getPhase()) || "remove".equals(controllDto.getPhase())) {

	    		closeNumber = 0;

	    		//オープンクローズ順の最大を取得する
	    		stmt2 = conn.createStatement();
				String sql2 = "SELECT OPEN_CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "'"
						+ " AND OPEN_CLOSE_NUMBER = "
						+ "(SELECT MIN(OPEN_CLOSE_NUMBER) AS OPEN_CLOSE_NUMBER FROM BATTLE_FIELD WHERE BATTLE_ID = '" + battleID + "' AND OPEN_CLOSE_NUMBER > 0"
						+ " AND CARD_ID IS NOT NULL) "
						+ " AND CARD_ID IS NOT NULL ";

		    	rs2 = stmt2.executeQuery(sql2);

		    	//最小のクローズ順を取得
		    	if (rs2.next()) {
		    		closeNumber = rs2.getInt("OPEN_CLOSE_NUMBER");
		    	}

	    		//最大クローズ順＋１を設定する
		    	fieldDto.setOpen_close_number(closeNumber + 1);
	    	} else {
	    		//最大クローズ順＋１を設定する
		    	fieldDto.setClose_number(closeNumber + 1);
	    	}

    	} finally {
    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	private BattleFieldDTO getCloseDto(ArrayList<BattleFieldDTO> list) {

		BattleFieldDTO ret = new BattleFieldDTO();

		ArrayList<BattleFieldDTO> ramList = new ArrayList<BattleFieldDTO>();

    	//複数存在する場合は、複数同時クローズ
    	if (list.size() > 1) {

    		int minfieldNumber = 0;

    		for (int i = 0; i < list.size(); i++) {

    			int fieldNumber = list.get(i).getField_no();

    			if (i == 0) {
    				minfieldNumber = fieldNumber;
    				ramList.add(list.get(i));

    			} else {
    				if (fieldNumber < minfieldNumber) {
	    				minfieldNumber = fieldNumber;
	    				ramList = new ArrayList<BattleFieldDTO>();
	    				ramList.add(list.get(i));

	    			} else if (fieldNumber == minfieldNumber) {
	    				ramList.add(list.get(i));
	    			}
    			}
    		}

    		//複数いるならランダム
    		if (ramList.size() > 1) {

    				Random rand = new Random();
					int num = rand.nextInt(ramList.size());

					ret = ramList.get(num);

    		} else {

    			//対象が一人ならそのまま返却
    			ret = ramList.get(0);

    		}

    	} else if (list.size() == 1) {

    		//対象が一人ならそのまま返却
    		ret = list.get(0);
    	}

		return ret;
	}
}
