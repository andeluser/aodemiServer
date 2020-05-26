package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dto.BattleDeckDTO;
import util.AodemiLogger;
import util.DBManeger;

public class BattleDeckDAOImpl implements BattleDeckDAO {

	public BattleDeckDTO getAllValue(String battleID, String playerId, int deckNumber) throws Exception {

		BattleDeckDTO ret = new BattleDeckDTO();
		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_DECK WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "' AND DECK_NO ='" + deckNumber + "'";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		ret.setBattle_id(rs.getString("BATTLE_ID"));
	    		ret.setPlayer_id(rs.getString("PLAYER_ID"));
	    		ret.setDeck_no(rs.getInt("DECK_NO"));
	    		ret.setSub_no(rs.getInt("SUB_NO"));
	    		ret.setNew_flg(rs.getInt("NEW_FLG"));
	    		ret.setCard_id(rs.getString("CARD_ID"));
	    		ret.setColor(rs.getString("COLOR"));
	    		ret.setCard_type(rs.getString("CARD_TYPE"));
	    		ret.setCard_type1(rs.getString("CARD_TYPE1"));
	    		ret.setCard_type2(rs.getString("CARD_TYPE2"));
	    		ret.setLevel(rs.getInt("LEVEL"));
	    		ret.setStock(rs.getInt("STOCK"));
	    		ret.setHp(rs.getInt("HP"));
	    		ret.setAtk(rs.getInt("ATK"));
	    		ret.setDef(rs.getInt("DEF"));
	    		ret.setAgi(rs.getInt("AGI"));
	    		ret.setRng(rs.getInt("RNG"));
	    		ret.setCard_lock(rs.getInt("CARD_LOCK"));
	    		ret.setCard_out(rs.getInt("CARD_OUT"));
	    		ret.setOpen_skill(rs.getInt("OPEN_SKILL"));
	    		ret.setStart_skill(rs.getInt("START_SKILL"));
	    		ret.setClose_skill(rs.getInt("CLOSE_SKILL"));
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

	public ArrayList<BattleDeckDTO> getAllList(String battleID, String playerId) throws Exception {

		ArrayList<BattleDeckDTO> list = new ArrayList<BattleDeckDTO>();

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_DECK WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "' ORDER BY DECK_NO";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		BattleDeckDTO dto = new BattleDeckDTO();

	    		dto.setBattle_id(rs.getString("BATTLE_ID"));
	    		dto.setPlayer_id(rs.getString("PLAYER_ID"));
	    		dto.setDeck_no(rs.getInt("DECK_NO"));
	    		dto.setSub_no(rs.getInt("SUB_NO"));
	    		dto.setNew_flg(rs.getInt("NEW_FLG"));
	    		dto.setCard_id(rs.getString("CARD_ID"));
	    		dto.setColor(rs.getString("COLOR"));
	    		dto.setCard_type(rs.getString("CARD_TYPE"));
	    		dto.setCard_type1(rs.getString("CARD_TYPE1"));
	    		dto.setCard_type2(rs.getString("CARD_TYPE2"));
	    		dto.setStock(rs.getInt("STOCK"));
	    		dto.setLevel(rs.getInt("LEVEL"));
	    		dto.setHp(rs.getInt("HP"));
	    		dto.setAtk(rs.getInt("ATK"));
	    		dto.setDef(rs.getInt("DEF"));
	    		dto.setAgi(rs.getInt("AGI"));
	    		dto.setRng(rs.getInt("RNG"));
	    		dto.setCard_lock(rs.getInt("CARD_LOCK"));
	    		dto.setCard_out(rs.getInt("CARD_OUT"));
	    		dto.setOpen_skill(rs.getInt("OPEN_SKILL"));
	    		dto.setStart_skill(rs.getInt("START_SKILL"));
	    		dto.setClose_skill(rs.getInt("CLOSE_SKILL"));
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

	public int insert(BattleDeckDTO dto) throws Exception {

		int ret = 0;

    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "INSERT INTO BATTLE_DECK VALUES ("
					+ "'" + dto.getBattle_id() + "'"
					+ ", '" + dto.getPlayer_id() + "'"
					+ ", '" + dto.getDeck_no() + "'"
					+ ", '" + dto.getSub_no() + "'"
					+ ", '" + dto.getNew_flg() + "'"
					+ ", '" + dto.getCard_id() + "'"
					+ ", '" + dto.getColor() + "'"
					+ ", '" + dto.getCard_type() + "'"
					+ ", '" + dto.getCard_type1() + "'"
					+ ", '" + dto.getCard_type2() + "'"
					+ ", '" + dto.getLevel() + "'"
					+ ", '" + dto.getStock() + "'"
					+ ", '" + dto.getHp() + "'"
					+ ", '" + dto.getAtk() + "'"
					+ ", '" + dto.getDef() + "'"
					+ ", '" + dto.getAgi() + "'"
					+ ", '" + dto.getRng() + "'"
					+ ", '" + dto.getCard_lock() + "'"
					+ ", '" + dto.getCard_out() + "'"
					+ ", '" + dto.getOpen_skill() + "'"
					+ ", '" + dto.getStart_skill() + "'"
					+ ", '" + dto.getClose_skill() + "'"
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

	public int deleteBattle(BattleDeckDTO dto) throws Exception {

		int ret = 0;
    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "DELETE FROM BATTLE_DECK WHERE PLAYER_ID = '" + dto.getPlayer_id() + "'";

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

	public int update(BattleDeckDTO dto) throws Exception {

		int ret = 0;
		DBManeger accesser = new DBManeger();
    	Connection conn = accesser.getConnection();

    	Statement stmt = null;

    	try {
	    	stmt = conn.createStatement();
			String sql = "UPDATE BATTLE_DECK SET "
//					+ ", PLAYER_ID = '" + dto.getPlayer_id() + "'"
//					+ ", DECK_NO = '" + dto.getDeck_no() + "'"
//					+ ", SUB_NO = '" + dto.getSub_no() + "'"
//					+ ", NEW_FLG = '" + dto.getNew_flg() + "'"
					+ "CARD_ID = '" + dto.getCard_id() + "'"
					+ ", COLOR = '" + dto.getColor() + "'"
					+ ", CARD_TYPE = '" + dto.getCard_type() + "'"
					+ ", CARD_TYPE1 = '" + dto.getCard_type1() + "'"
					+ ", CARD_TYPE2 = '" + dto.getCard_type2() + "'"
					+ ", LEVEL = '" + dto.getLevel() + "'"
					+ ", STOCK = '" + dto.getStock() + "'"
					+ ", HP = '" + dto.getHp() + "'"
					+ ", ATK = '" + dto.getAtk() + "'"
					+ ", DEF = '" + dto.getDef() + "'"
					+ ", AGI = '" + dto.getAgi() + "'"
					+ ", RNG = '" + dto.getRng() + "'"
					+ ", CARD_LOCK = '" + dto.getCard_lock() + "'"
					+ ", CARD_OUT = '" + dto.getCard_out() + "'"
					+ ", OPEN_SKILL = '" + dto.getOpen_skill() + "'"
					+ ", START_SKILL = '" + dto.getStart_skill() + "'"
					+ ", CLOSE_SKILL = '" + dto.getClose_skill() + "'"
					+ ", UPDATE_DATE = CURRENT_TIMESTAMP"
					+ " WHERE BATTLE_ID = '" + dto.getBattle_id() + "' AND PLAYER_ID = '" + dto.getPlayer_id()+ "' AND DECK_NO ='" + dto.getDeck_no() + "'";

			AodemiLogger.writeLogOnly(sql);

	    	ret = stmt.executeUpdate(sql);
	     	conn.commit();

	     	return ret;

    	} finally {

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

	}

}
