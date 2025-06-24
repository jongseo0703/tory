package com.sinse.tory.rightpage.datamodificationpage;

import com.sinse.tory.rightpage.view.ShowMessage;

// 수정 창에서 수정한 데이터를 DB에서 관리하는 기능
final class DataManagementFromTable
{
	private ImageUploadPanel imageUploadPanel;
	private IdentificationPanel identificationPanel;
	private DetailDataTable detailDataTable;
	
	
	
	public DataManagementFromTable(ImageUploadPanel imageUploadPanel, IdentificationPanel identificationPanel, DetailDataTable detailDataTable)
	{
		this.imageUploadPanel = imageUploadPanel;
		this.identificationPanel = identificationPanel;
		this.detailDataTable = detailDataTable;
	}
	
	
	
	void add()
	{
		if (identificationPanel.isSelectAll() && detailDataTable.isInputAll())
		{
			
		}
		else
		{
			ShowMessage.showAlert(null, "상품 추가", "상품을 추가 하시겠습니까?");
		}
	}
}