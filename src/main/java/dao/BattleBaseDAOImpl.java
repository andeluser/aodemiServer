package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import dto.BattleBaseDTO;
import util.AodemiLogger;
import util.DBManeger;

public class BattleBaseDAOImpl implements BattleBaseDAO {

	public BattleBaseDTO getAllValue(String battleID, String playerId) throws Exception {

		BattleBaseDTO ret = new BattleBaseDTO();
		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_BASE WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "'";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		ret.setBattle_id(rs.getString("BATTLE_ID"));
	    		ret.setPlayer_id(rs.getString("PLAYER_ID"));
	    		ret.setSub_no(rs.getInt("SUB_NO"));
	    		ret.setNew_flg(rs.getInt("NEW_FLG"));
	    		ret.setYellow(rs.getInt("YELLOW"));
	    		ret.setBlack(rs.getInt("BLACK"));
	    		ret.setRed(rs.getInt("RED"));
	    		ret.setBlue(rs.getInt("BLUE"));
	    		ret.setSpecial_gage(rs.getInt("SPECIAL_GAGE"));
	    		ret.setSpecial_stock(rs.getInt("SPECIAL_STOCK"));
	    		ret.setSpecial_use(rs.getInt("SPECIAL_USE"));
	    		ret.setLife(rs.getInt("LIFE"));
	    		ret.setSet_card(rs.getString("SET_CARD"));
	    		ret.setSet_deck_no(rs.getInt("SET_DECK_NO"));
	    		ret.setCemetery(rs.getString("CEMETERY"));
	    		ret.setDisappearance(rs.getString("DISAPPEARANCE"));
	    		ret.setSp(rs.getInt("SP"));
	    		ret.setMagic(rs.getInt("MAGIC"));
	    		ret.setDivine(rs.getInt("DIVINE"));
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

	public int insert(BattleBaseDTO dto) throws Exception {

		int ret = 0;

    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "INSERT INTO BATTLE_BASE VALUES ("
					+ "'" + dto.getBattle_id() + "'"
					+ ", '" + dto.getPlayer_id() + "'"
					+ ", '" + dto.getSub_no() + "'"
					+ ", '" + dto.getNew_flg() + "'"
					+ ", '" + dto.getYellow() + "'"
					+ ", '" + dto.getBlack() + "'"
					+ ", '" + dto.getRed() + "'"
					+ ", '" + dto.getBlue() + "'"
					+ ", '" + dto.getSpecial_gage() + "'"
					+ ", '" + dto.getSpecial_stock() + "'"
					+ ", '" + dto.getSpecial_use() + "'"
					+ ", '" + dto.getLife() + "'"
					+ ", '" + dto.getSet_card() + "'"
					+ ", '" + dto.getSet_deck_no() + "'"
					+ ", '" + dto.getCemetery() + "'"
					+ ", '" + dto.getDisappearance() + "'"
					+ ", '" + dto.getSp() + "'"
					+ ", '" + dto.getMagic() + "'"
					+ ", '" + dto.getDivine() + "'"
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

	public int deleteBattle(BattleBaseDTO dto) throws Exception {

		int ret = 0;
    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "DELETE FROM BATTLE_BASE WHERE PLAYER_ID = '" + dto.getPlayer_id() + "'";

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

	public int update(BattleBaseDTO dto) throws Exception {

		int ret = 0;
		DBManeger accesser = new DBManeger();
    	Connection conn = accesser.getConnection();

    	Statement stmt = null;

    	try {
        	stmt = conn.createStatement();
    		String sql = "UPDATE BATTLE_BASE SET "
    				+ "YELLOW = '" + dto.getYellow() + "'"
    				+ ", BLACK = '" + dto.getBlack() + "'"
    				+ ", RED = '" + dto.getRed() + "'"
    				+ ", BLUE = '" + dto.getBlue() + "'"
    				+ ", SPECIAL_GAGE = '" + dto.getSpecial_gage() + "'"
    				+ ", SPECIAL_STOCK = '" + dto.getSpecial_stock() + "'"
    				+ ", SPECIAL_USE = '" + dto.getSpecial_use() + "'"
    				+ ", LIFE = '" + dto.getLife() + "'"
    				+ ", SET_CARD = '" + dto.getSet_card() + "'"
    				+ ", SET_DECK_NO = '" + dto.getSet_deck_no() + "'"
    				+ ", CEMETERY = '" + dto.getCemetery() + "'"
    				+ ", DISAPPEARANCE = '" + dto.getDisappearance() + "'"
    				+ ", SP = '" + dto.getSp() + "'"
    				+ ", MAGIC = '" + dto.getMagic() + "'"
    				+ ", DIVINE = '" + dto.getDivine() + "'"
    				+ ", UPDATE_DATE = CURRENT_TIMESTAMP"
    				+ " WHERE BATTLE_ID = '" + dto.getBattle_id() + "' AND PLAYER_ID = '" + dto.getPlayer_id()+ "'";
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

}
