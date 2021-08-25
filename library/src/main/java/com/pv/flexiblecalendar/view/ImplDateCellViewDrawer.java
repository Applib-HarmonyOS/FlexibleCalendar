package com.pv.flexiblecalendar.view;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * IDateCellViewDrawer.
 *
 * @author p-v
 */
public interface ImplDateCellViewDrawer extends ImplCellViewDrawer {
    /**
     * Date Cell view.
     *
     * @param position position
     * @param convertView convertview
     * @param parent parent
     * @param cellType celltype
     * @return getcellview
     */
    BaseCellView getCellView(int position, Component convertView, ComponentContainer parent,
                             @BaseCellView.CellType int cellType);
}
