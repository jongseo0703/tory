package com.sinse.tory.rightpage.datamodificationpage;

import com.sinse.tory.db.model.Brand;
import com.sinse.tory.db.model.Location;
import com.sinse.tory.db.model.ProductDetail;
import com.sinse.tory.db.model.SubCategory;
import com.sinse.tory.db.model.TopCategory;
import com.sinse.tory.db.repository.ProductDetailDAO;
import com.sinse.tory.rightpage.db.repository.RightPageProductDetailDAO;
import com.sinse.tory.rightpage.util.PageMove;
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
	
	
	
	void add(PageMove pageMove)
	{
		if (detailDataTable.isInputAll() && identificationPanel.isSelectAll())
		{
			SubCategory subCategory = identificationPanel.createSubCategoryFromInputted();
			ProductDetail productDetail = detailDataTable.createProductDetailFromInputted(subCategory, identificationPanel.getProductName());
			
			if (ShowMessage.showConfirm(null, "상품 추가", "상품을 추가 하시겠습니까?") == false)
			{
				return;
			}
			
			RightPageProductDetailDAO.insert(productDetail);
			pageMove.showPage(0, 1);
		}
		else
		{
			ShowMessage.showAlert(null, "상품 추가", "상품 정보를 모두 입력해주세요.");
		}
	}
}