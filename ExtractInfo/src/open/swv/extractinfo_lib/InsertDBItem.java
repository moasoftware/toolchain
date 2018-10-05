package open.swv.extractinfo_lib;

import java.util.Collection;

public class InsertDBItem {
	private String preparedStatement = null;
	private Collection<String> dataList = null;

	public InsertDBItem(Collection<String> list) {
		setDataList(list);
	}

	public void setPreparedStatement(String i)
	{
		preparedStatement = i;
	}

	public void setDataList(Collection<String> list)
	{
		dataList = list;
	}

	public String getPreparedStatement()
	{
		return preparedStatement;
	}

	public Collection<String> getDataList()
	{
		return dataList;
	}
}
