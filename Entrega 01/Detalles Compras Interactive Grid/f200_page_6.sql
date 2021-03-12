prompt --application/set_environment
set define off verify off feedback off
whenever sqlerror exit sql.sqlcode rollback
--------------------------------------------------------------------------------
--
-- ORACLE Application Express (APEX) export file
--
-- You should run the script connected to SQL*Plus as the Oracle user
-- APEX_050100 or as the owner (parsing schema) of the application.
--
-- NOTE: Calls to apex_application_install override the defaults below.
--
--------------------------------------------------------------------------------
begin
wwv_flow_api.import_begin (
 p_version_yyyy_mm_dd=>'2016.08.24'
,p_release=>'5.1.4.00.08'
,p_default_workspace_id=>1841243641042122
,p_default_application_id=>200
,p_default_owner=>'OEHR'
);
end;
/
 
prompt APPLICATION 200 - Entrega01
--
-- Application Export:
--   Application:     200
--   Name:            Entrega01
--   Date and Time:   11:42 Friday March 12, 2021
--   Exported By:     ADMIN
--   Flashback:       0
--   Export Type:     Page Export
--   Version:         5.1.4.00.08
--   Instance ID:     220105491229916
--

prompt --application/pages/delete_00006
begin
wwv_flow_api.remove_page (p_flow_id=>wwv_flow.g_flow_id, p_page_id=>6);
end;
/
prompt --application/pages/page_00006
begin
wwv_flow_api.create_page(
 p_id=>6
,p_user_interface_id=>wwv_flow_api.id(15933951538602951)
,p_name=>'Compras'
,p_page_mode=>'NORMAL'
,p_step_title=>'Compras'
,p_step_sub_title_type=>'TEXT_WITH_SUBSTITUTIONS'
,p_first_item=>'NO_FIRST_ITEM'
,p_autocomplete_on_off=>'OFF'
,p_javascript_code=>wwv_flow_string.join(wwv_flow_t_varchar2(
'var htmldb_delete_message=''"DELETE_CONFIRM_MSG"'';',
'var htmldb_ch_message=''"OK_TO_GET_NEXT_PREV_PK_VALUE"'';'))
,p_page_template_options=>'#DEFAULT#'
,p_overwrite_navigation_list=>'N'
,p_page_is_public_y_n=>'N'
,p_protection_level=>'C'
,p_cache_mode=>'NOCACHE'
,p_last_updated_by=>'ADMIN'
,p_last_upd_yyyymmddhh24miss=>'20210312113550'
);
wwv_flow_api.create_page_plug(
 p_id=>wwv_flow_api.id(5606340507490045)
,p_plug_name=>'Detalle de Compra'
,p_region_template_options=>'#DEFAULT#'
,p_component_template_options=>'#DEFAULT#'
,p_region_attributes=>'style="width: 90%;"'
,p_plug_template=>wwv_flow_api.id(15899799650602919)
,p_plug_display_sequence=>20
,p_include_in_reg_disp_sel_yn=>'Y'
,p_plug_display_point=>'BODY'
,p_plug_source=>wwv_flow_string.join(wwv_flow_t_varchar2(
'select',
'    ORDER_ITEM_ID, -- pk',
'    ORDER_ID,      -- fk: Orders',
'    LINE_ITEM_ID,',
'    PRODUCT_ID,    -- fk: Product Info',
'    UNIT_PRICE,',
'    QUANTITY',
'                    -- Order_id + Product_id = Unique',
'from',
'    OEHR_ORDER_ITEMS',
'where',
'    ORDER_ID = :P6_ORDER_ID',
'',
''))
,p_plug_source_type=>'NATIVE_IG'
,p_ajax_items_to_submit=>'P6_ORDER_ID'
,p_plug_query_options=>'DERIVED_REPORT_COLUMNS'
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5606509687490047)
,p_name=>'ORDER_ID'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'ORDER_ID'
,p_data_type=>'NUMBER'
,p_is_query_only=>false
,p_item_type=>'NATIVE_HIDDEN'
,p_display_sequence=>40
,p_attribute_01=>'Y'
,p_filter_is_required=>false
,p_use_as_row_header=>false
,p_enable_sort_group=>true
,p_is_primary_key=>false
,p_default_type=>'ITEM'
,p_default_expression=>'P6_ORDER_ID'
,p_duplicate_value=>true
,p_include_in_export=>false
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5606689686490048)
,p_name=>'LINE_ITEM_ID'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'LINE_ITEM_ID'
,p_data_type=>'NUMBER'
,p_is_query_only=>false
,p_item_type=>'NATIVE_DISPLAY_ONLY'
,p_heading=>'Line item id'
,p_heading_alignment=>'RIGHT'
,p_display_sequence=>50
,p_value_alignment=>'RIGHT'
,p_attribute_02=>'VALUE'
,p_enable_filter=>true
,p_filter_is_required=>false
,p_filter_lov_type=>'NONE'
,p_use_as_row_header=>false
,p_enable_sort_group=>true
,p_is_primary_key=>false
,p_duplicate_value=>true
,p_include_in_export=>true
,p_escape_on_http_output=>true
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5606732290490049)
,p_name=>'PRODUCT_ID'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'PRODUCT_ID'
,p_data_type=>'NUMBER'
,p_is_query_only=>false
,p_item_type=>'NATIVE_POPUP_LOV'
,p_heading=>'Product id'
,p_heading_alignment=>'RIGHT'
,p_display_sequence=>60
,p_value_alignment=>'RIGHT'
,p_attribute_01=>'NOT_ENTERABLE'
,p_attribute_02=>'FIRST_ROWSET'
,p_is_required=>true
,p_lov_type=>'SHARED'
,p_lov_id=>wwv_flow_api.id(5985578806848182)
,p_lov_display_extra=>true
,p_lov_display_null=>true
,p_enable_filter=>true
,p_filter_is_required=>false
,p_filter_lov_type=>'LOV'
,p_use_as_row_header=>false
,p_enable_sort_group=>true
,p_is_primary_key=>false
,p_duplicate_value=>true
,p_include_in_export=>true
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5606840848490050)
,p_name=>'UNIT_PRICE'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'UNIT_PRICE'
,p_data_type=>'NUMBER'
,p_is_query_only=>false
,p_item_type=>'NATIVE_NUMBER_FIELD'
,p_heading=>'Unit price'
,p_heading_alignment=>'RIGHT'
,p_display_sequence=>70
,p_value_alignment=>'RIGHT'
,p_attribute_03=>'right'
,p_is_required=>false
,p_enable_filter=>true
,p_filter_is_required=>false
,p_filter_lov_type=>'NONE'
,p_use_as_row_header=>false
,p_enable_sort_group=>true
,p_is_primary_key=>false
,p_duplicate_value=>true
,p_include_in_export=>true
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5702421034547001)
,p_name=>'QUANTITY'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'QUANTITY'
,p_data_type=>'NUMBER'
,p_is_query_only=>false
,p_item_type=>'NATIVE_NUMBER_FIELD'
,p_heading=>'Quantity'
,p_heading_alignment=>'RIGHT'
,p_display_sequence=>80
,p_value_alignment=>'RIGHT'
,p_attribute_03=>'right'
,p_is_required=>false
,p_enable_filter=>true
,p_filter_is_required=>false
,p_filter_lov_type=>'NONE'
,p_use_as_row_header=>false
,p_enable_sort_group=>true
,p_is_primary_key=>false
,p_duplicate_value=>true
,p_include_in_export=>true
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5702531050547002)
,p_name=>'ROWID'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'ROWID'
,p_data_type=>'ROWID'
,p_item_type=>'NATIVE_HIDDEN'
,p_display_sequence=>30
,p_attribute_01=>'Y'
,p_use_as_row_header=>false
,p_enable_sort_group=>false
,p_is_primary_key=>true
,p_include_in_export=>false
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5702620048547003)
,p_name=>'APEX$ROW_ACTION'
,p_item_type=>'NATIVE_ROW_ACTION'
,p_display_sequence=>20
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5702752257547004)
,p_name=>'APEX$ROW_SELECTOR'
,p_item_type=>'NATIVE_ROW_SELECTOR'
,p_display_sequence=>10
,p_attribute_01=>'Y'
,p_attribute_02=>'Y'
,p_attribute_03=>'N'
);
wwv_flow_api.create_region_column(
 p_id=>wwv_flow_api.id(5702967417547006)
,p_name=>'ORDER_ITEM_ID'
,p_source_type=>'DB_COLUMN'
,p_source_expression=>'ORDER_ITEM_ID'
,p_data_type=>'NUMBER'
,p_is_query_only=>true
,p_item_type=>'NATIVE_HIDDEN'
,p_display_sequence=>90
,p_attribute_01=>'Y'
,p_filter_is_required=>false
,p_use_as_row_header=>false
,p_enable_sort_group=>true
,p_is_primary_key=>true
,p_include_in_export=>false
);
wwv_flow_api.create_interactive_grid(
 p_id=>wwv_flow_api.id(5606414985490046)
,p_internal_uid=>5606414985490046
,p_is_editable=>true
,p_edit_operations=>'i:u:d'
,p_lost_update_check_type=>'VALUES'
,p_add_row_if_empty=>true
,p_submit_checked_rows=>false
,p_lazy_loading=>false
,p_requires_filter=>false
,p_show_nulls_as=>'-'
,p_pagination_type=>'SCROLL'
,p_show_total_row_count=>true
,p_show_toolbar=>true
,p_enable_save_public_report=>false
,p_enable_subscriptions=>true
,p_enable_download=>true
,p_enable_mail_download=>true
,p_fixed_header=>'PAGE'
,p_show_icon_view=>false
,p_show_detail_view=>false
);
wwv_flow_api.create_ig_report(
 p_id=>wwv_flow_api.id(5709171459554158)
,p_interactive_grid_id=>wwv_flow_api.id(5606414985490046)
,p_type=>'PRIMARY'
,p_default_view=>'GRID'
,p_show_row_number=>false
,p_settings_area_expanded=>true
);
wwv_flow_api.create_ig_report_view(
 p_id=>wwv_flow_api.id(5709222766554158)
,p_report_id=>wwv_flow_api.id(5709171459554158)
,p_view_type=>'GRID'
,p_srv_exclude_null_values=>false
,p_srv_only_display_columns=>true
,p_edit_mode=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5709726324554160)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>1
,p_column_id=>wwv_flow_api.id(5606509687490047)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5710236788554162)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>3
,p_column_id=>wwv_flow_api.id(5606689686490048)
,p_is_visible=>true
,p_is_frozen=>false
,p_sort_order=>1
,p_sort_direction=>'ASC'
,p_sort_nulls=>'LAST'
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5710757592554164)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>3
,p_column_id=>wwv_flow_api.id(5606732290490049)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5711294416554164)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>4
,p_column_id=>wwv_flow_api.id(5606840848490050)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5711734799554166)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>5
,p_column_id=>wwv_flow_api.id(5702421034547001)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5716485364618412)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>6
,p_column_id=>wwv_flow_api.id(5702531050547002)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5716927966618416)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>0
,p_column_id=>wwv_flow_api.id(5702620048547003)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_ig_report_column(
 p_id=>wwv_flow_api.id(5720529335631728)
,p_view_id=>wwv_flow_api.id(5709222766554158)
,p_display_seq=>7
,p_column_id=>wwv_flow_api.id(5702967417547006)
,p_is_visible=>true
,p_is_frozen=>false
);
wwv_flow_api.create_page_plug(
 p_id=>wwv_flow_api.id(5818781557374051)
,p_plug_name=>'Maestro de compras'
,p_region_template_options=>'#DEFAULT#:t-Region--scrollBody'
,p_plug_template=>wwv_flow_api.id(15900290869602920)
,p_plug_display_sequence=>10
,p_include_in_reg_disp_sel_yn=>'N'
,p_plug_display_point=>'BODY'
,p_plug_query_row_template=>1
,p_plug_query_options=>'DERIVED_REPORT_COLUMNS'
,p_attribute_01=>'N'
,p_attribute_02=>'TEXT'
,p_attribute_03=>'Y'
);
wwv_flow_api.create_page_plug(
 p_id=>wwv_flow_api.id(5825876555374053)
,p_plug_name=>'Breadcrumb'
,p_region_template_options=>'#DEFAULT#:t-BreadcrumbRegion--useBreadcrumbTitle'
,p_component_template_options=>'#DEFAULT#'
,p_plug_template=>wwv_flow_api.id(15904760843602920)
,p_plug_display_sequence=>10
,p_include_in_reg_disp_sel_yn=>'N'
,p_plug_display_point=>'REGION_POSITION_01'
,p_menu_id=>wwv_flow_api.id(15935250282602968)
,p_plug_source_type=>'NATIVE_BREADCRUMB'
,p_menu_template_id=>wwv_flow_api.id(15923627256602933)
,p_plug_query_row_template=>1
);
wwv_flow_api.create_page_button(
 p_id=>wwv_flow_api.id(5819280740374051)
,p_button_sequence=>30
,p_button_plug_id=>wwv_flow_api.id(5818781557374051)
,p_button_name=>'SAVE'
,p_button_action=>'SUBMIT'
,p_button_template_options=>'#DEFAULT#'
,p_button_template_id=>wwv_flow_api.id(15923096367602933)
,p_button_is_hot=>'Y'
,p_button_image_alt=>'Guardar'
,p_button_position=>'REGION_TEMPLATE_CHANGE'
,p_button_condition=>'P6_ORDER_ID'
,p_button_condition_type=>'ITEM_IS_NOT_NULL'
,p_grid_new_grid=>false
,p_database_action=>'UPDATE'
);
wwv_flow_api.create_page_button(
 p_id=>wwv_flow_api.id(5826968370374053)
,p_button_sequence=>50
,p_button_plug_id=>wwv_flow_api.id(5818781557374051)
,p_button_name=>'GET_PREVIOUS_ORDER_ID'
,p_button_action=>'SUBMIT'
,p_button_template_options=>'#DEFAULT#'
,p_button_template_id=>wwv_flow_api.id(15922998912602932)
,p_button_image_alt=>'Anterior'
,p_button_position=>'REGION_TEMPLATE_CHANGE'
,p_warn_on_unsaved_changes=>null
,p_button_condition=>'P6_ORDER_ID_PREV'
,p_button_condition_type=>'ITEM_IS_NOT_NULL'
,p_icon_css_classes=>'fa-chevron-left'
,p_grid_new_grid=>false
,p_button_comment=>'This button is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_button(
 p_id=>wwv_flow_api.id(5826840597374053)
,p_button_sequence=>60
,p_button_plug_id=>wwv_flow_api.id(5818781557374051)
,p_button_name=>'GET_NEXT_ORDER_ID'
,p_button_action=>'SUBMIT'
,p_button_template_options=>'#DEFAULT#'
,p_button_template_id=>wwv_flow_api.id(15922998912602932)
,p_button_image_alt=>'Posterior'
,p_button_position=>'REGION_TEMPLATE_CHANGE'
,p_warn_on_unsaved_changes=>null
,p_button_condition=>'P6_ORDER_ID_NEXT'
,p_button_condition_type=>'ITEM_IS_NOT_NULL'
,p_icon_css_classes=>'fa-chevron-right'
,p_grid_new_grid=>false
,p_button_comment=>'This button is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_button(
 p_id=>wwv_flow_api.id(5819463377374051)
,p_button_sequence=>10
,p_button_plug_id=>wwv_flow_api.id(5818781557374051)
,p_button_name=>'CANCEL'
,p_button_action=>'REDIRECT_PAGE'
,p_button_template_options=>'#DEFAULT#'
,p_button_template_id=>wwv_flow_api.id(15923096367602933)
,p_button_image_alt=>'Cancelar'
,p_button_position=>'REGION_TEMPLATE_CLOSE'
,p_button_redirect_url=>'f?p=&APP_ID.:5:&SESSION.::&DEBUG.:::'
,p_grid_new_grid=>false
);
wwv_flow_api.create_page_button(
 p_id=>wwv_flow_api.id(5819342093374051)
,p_button_sequence=>20
,p_button_plug_id=>wwv_flow_api.id(5818781557374051)
,p_button_name=>'DELETE'
,p_button_action=>'REDIRECT_URL'
,p_button_template_options=>'#DEFAULT#'
,p_button_template_id=>wwv_flow_api.id(15923096367602933)
,p_button_image_alt=>'Borrar'
,p_button_position=>'REGION_TEMPLATE_DELETE'
,p_button_redirect_url=>'javascript:apex.confirm(htmldb_delete_message,''DELETE'');'
,p_button_execute_validations=>'N'
,p_button_condition=>'P6_ORDER_ID'
,p_button_condition_type=>'ITEM_IS_NOT_NULL'
,p_grid_new_grid=>false
,p_database_action=>'DELETE'
);
wwv_flow_api.create_page_branch(
 p_id=>wwv_flow_api.id(5826579159374053)
,p_branch_action=>'f?p=&APP_ID.:5:&SESSION.&success_msg=#SUCCESS_MSG#'
,p_branch_point=>'AFTER_PROCESSING'
,p_branch_type=>'REDIRECT_URL'
,p_branch_sequence=>1
,p_branch_condition_type=>'REQUEST_IN_CONDITION'
,p_branch_condition=>'SAVE,DELETE,CREATE'
);
wwv_flow_api.create_page_branch(
 p_id=>wwv_flow_api.id(5829597782374055)
,p_branch_action=>'f?p=&APP_ID.:6:&SESSION.::&DEBUG.::P6_ORDER_ID:&P6_ORDER_ID_NEXT.'
,p_branch_point=>'BEFORE_COMPUTATION'
,p_branch_type=>'REDIRECT_URL'
,p_branch_when_button_id=>wwv_flow_api.id(5826840597374053)
,p_branch_sequence=>1
,p_branch_comment=>'This button is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_branch(
 p_id=>wwv_flow_api.id(5829937967374055)
,p_branch_action=>'f?p=&APP_ID.:6:&SESSION.::&DEBUG.::P6_ORDER_ID:&P6_ORDER_ID_PREV.'
,p_branch_point=>'BEFORE_COMPUTATION'
,p_branch_type=>'REDIRECT_URL'
,p_branch_when_button_id=>wwv_flow_api.id(5826968370374053)
,p_branch_sequence=>1
,p_branch_comment=>'This button is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5821051680374052)
,p_name=>'P6_ORDER_ID'
,p_item_sequence=>10
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_source=>'ORDER_ID'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_HIDDEN'
,p_protection_level=>'S'
,p_attribute_01=>'Y'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5821418707374052)
,p_name=>'P6_ORDER_DATE'
,p_is_required=>true
,p_item_sequence=>20
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_prompt=>'Fecha'
,p_format_mask=>'DD-MON-YYYY HH:MIAM'
,p_source=>'ORDER_DATE'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_DATE_PICKER'
,p_cSize=>32
,p_cMaxlength=>255
,p_field_template=>wwv_flow_api.id(15922873949602932)
,p_item_template_options=>'#DEFAULT#'
,p_attribute_04=>'button'
,p_attribute_05=>'N'
,p_attribute_07=>'NONE'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5822260186374052)
,p_name=>'P6_ORDER_MODE'
,p_item_sequence=>30
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_prompt=>'Modo'
,p_source=>'ORDER_MODE'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_RADIOGROUP'
,p_named_lov=>'LOV_ORDER_MODE'
,p_lov=>'.'||wwv_flow_api.id(10718843457481842)||'.'
,p_lov_display_null=>'YES'
,p_field_template=>wwv_flow_api.id(15922684701602932)
,p_item_template_options=>'#DEFAULT#'
,p_lov_display_extra=>'YES'
,p_attribute_01=>'1'
,p_attribute_02=>'NONE'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5822659016374052)
,p_name=>'P6_CUSTOMER_ID'
,p_is_required=>true
,p_item_sequence=>40
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_prompt=>'Cliente'
,p_source=>'CUSTOMER_ID'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_SELECT_LIST'
,p_named_lov=>'LOV_CUSTOMERS'
,p_lov=>wwv_flow_string.join(wwv_flow_t_varchar2(
'select distinct',
'    OEHR_CUSTOMERS.CUST_FIRST_NAME || '' '' ||',
'    OEHR_CUSTOMERS.CUST_LAST_NAME as "Cliente",',
'    OEHR_CUSTOMERS.CUSTOMER_ID',
' from OEHR_CUSTOMERS, OEHR_ORDERS ',
' where OEHR_CUSTOMERS.CUSTOMER_ID=OEHR_ORDERS.CUSTOMER_ID'))
,p_lov_display_null=>'YES'
,p_lov_null_text=>'No seleccionado'
,p_cHeight=>1
,p_field_template=>wwv_flow_api.id(15922873949602932)
,p_item_template_options=>'#DEFAULT#'
,p_lov_display_extra=>'YES'
,p_attribute_01=>'NONE'
,p_attribute_02=>'N'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5823075869374052)
,p_name=>'P6_ORDER_STATUS'
,p_item_sequence=>50
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_prompt=>'Estado'
,p_source=>'ORDER_STATUS'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_SELECT_LIST'
,p_named_lov=>'LOV_ORDER_STATUS'
,p_lov=>'.'||wwv_flow_api.id(10706880664268034)||'.'
,p_lov_display_null=>'YES'
,p_lov_null_text=>'No seleccionado'
,p_cHeight=>1
,p_field_template=>wwv_flow_api.id(15922684701602932)
,p_item_template_options=>'#DEFAULT#'
,p_lov_display_extra=>'YES'
,p_attribute_01=>'NONE'
,p_attribute_02=>'N'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5823469564374052)
,p_name=>'P6_ORDER_TOTAL'
,p_item_sequence=>60
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_prompt=>'Total'
,p_source=>'ORDER_TOTAL'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_DISPLAY_ONLY'
,p_field_template=>wwv_flow_api.id(15922684701602932)
,p_item_template_options=>'#DEFAULT#'
,p_attribute_01=>'Y'
,p_attribute_02=>'VALUE'
,p_attribute_04=>'Y'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5823880164374052)
,p_name=>'P6_SALES_REP_ID'
,p_is_required=>true
,p_item_sequence=>70
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_use_cache_before_default=>'NO'
,p_prompt=>'Vendedor'
,p_source=>'SALES_REP_ID'
,p_source_type=>'DB_COLUMN'
,p_display_as=>'NATIVE_SELECT_LIST'
,p_named_lov=>'LOV_SALES_REP'
,p_lov=>wwv_flow_string.join(wwv_flow_t_varchar2(
'select distinct ',
'       OEHR_EMPLOYEES.FIRST_NAME || '' '' ||',
'       OEHR_EMPLOYEES.LAST_NAME as "Representante",',
'       OEHR_EMPLOYEES.EMPLOYEE_ID',
' from OEHR_EMPLOYEES,',
'      OEHR_ORDERS ',
' where OEHR_ORDERS.SALES_REP_ID=OEHR_EMPLOYEES.EMPLOYEE_ID'))
,p_lov_display_null=>'YES'
,p_lov_null_text=>'No seleccionado'
,p_cHeight=>1
,p_field_template=>wwv_flow_api.id(15922873949602932)
,p_item_template_options=>'#DEFAULT#'
,p_lov_display_extra=>'YES'
,p_attribute_01=>'NONE'
,p_attribute_02=>'N'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5827337727374054)
,p_name=>'P6_ORDER_ID_NEXT'
,p_item_sequence=>90
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_display_as=>'NATIVE_HIDDEN'
,p_attribute_01=>'N'
,p_item_comment=>'This item is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5827719923374054)
,p_name=>'P6_ORDER_ID_PREV'
,p_item_sequence=>100
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_display_as=>'NATIVE_HIDDEN'
,p_attribute_01=>'N'
,p_item_comment=>'This item is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_item(
 p_id=>wwv_flow_api.id(5828150560374054)
,p_name=>'P6_ORDER_ID_COUNT'
,p_item_sequence=>80
,p_item_plug_id=>wwv_flow_api.id(5818781557374051)
,p_display_as=>'NATIVE_DISPLAY_ONLY'
,p_tag_attributes=>'class="fielddata"'
,p_attribute_01=>'Y'
,p_attribute_02=>'VALUE'
,p_attribute_04=>'N'
,p_item_comment=>'This item is needed for Get Next or Previous Primary Key Value process.'
);
wwv_flow_api.create_page_validation(
 p_id=>wwv_flow_api.id(5821938228374052)
,p_validation_name=>'P6_ORDER_DATE must be timestamp'
,p_validation_sequence=>20
,p_validation=>'P6_ORDER_DATE'
,p_validation_type=>'ITEM_IS_TIMESTAMP'
,p_error_message=>'#LABEL# must be a valid timestamp.'
,p_always_execute=>'N'
,p_associated_item=>wwv_flow_api.id(5821418707374052)
,p_error_display_location=>'INLINE_WITH_FIELD_AND_NOTIFICATION'
);
wwv_flow_api.create_page_process(
 p_id=>wwv_flow_api.id(5824699163374052)
,p_process_sequence=>10
,p_process_point=>'AFTER_HEADER'
,p_process_type=>'NATIVE_FORM_FETCH'
,p_process_name=>'Fetch Row from OEHR_ORDERS'
,p_attribute_02=>'OEHR_ORDERS'
,p_attribute_03=>'P6_ORDER_ID'
,p_attribute_04=>'ORDER_ID'
);
wwv_flow_api.create_page_process(
 p_id=>wwv_flow_api.id(5829137390374054)
,p_process_sequence=>20
,p_process_point=>'AFTER_HEADER'
,p_process_type=>'NATIVE_FORM_PAGINATION'
,p_process_name=>'Get Next or Previous Primary Key Value'
,p_attribute_02=>'OEHR_ORDERS'
,p_attribute_03=>'P6_ORDER_ID'
,p_attribute_04=>'ORDER_ID'
,p_attribute_07=>'ORDER_ID'
,p_attribute_09=>'P6_ORDER_ID_NEXT'
,p_attribute_10=>'P6_ORDER_ID_PREV'
,p_attribute_13=>'P6_ORDER_ID_COUNT'
);
wwv_flow_api.create_page_process(
 p_id=>wwv_flow_api.id(5825037146374052)
,p_process_sequence=>30
,p_process_point=>'AFTER_SUBMIT'
,p_process_type=>'NATIVE_FORM_PROCESS'
,p_process_name=>'Process Row of OEHR_ORDERS'
,p_attribute_02=>'OEHR_ORDERS'
,p_attribute_03=>'P6_ORDER_ID'
,p_attribute_04=>'ORDER_ID'
,p_attribute_11=>'I:U:D'
,p_error_display_location=>'INLINE_IN_NOTIFICATION'
,p_process_success_message=>'Action Processed.'
);
wwv_flow_api.create_page_process(
 p_id=>wwv_flow_api.id(5825414718374053)
,p_process_sequence=>40
,p_process_point=>'AFTER_SUBMIT'
,p_process_type=>'NATIVE_SESSION_STATE'
,p_process_name=>'reset page'
,p_attribute_01=>'CLEAR_CACHE_CURRENT_PAGE'
,p_error_display_location=>'INLINE_IN_NOTIFICATION'
,p_process_when_button_id=>wwv_flow_api.id(5819342093374051)
);
wwv_flow_api.create_page_process(
 p_id=>wwv_flow_api.id(5702896990547005)
,p_process_sequence=>50
,p_process_point=>'AFTER_SUBMIT'
,p_region_id=>wwv_flow_api.id(5606340507490045)
,p_process_type=>'NATIVE_IG_DML'
,p_process_name=>'New - Save Interactive Grid Data'
,p_attribute_01=>'REGION_SOURCE'
,p_attribute_05=>'Y'
,p_attribute_06=>'Y'
,p_attribute_08=>'Y'
,p_error_display_location=>'INLINE_IN_NOTIFICATION'
);
end;
/
begin
wwv_flow_api.import_end(p_auto_install_sup_obj => nvl(wwv_flow_application_install.get_auto_install_sup_obj, false));
commit;
end;
/
set verify on feedback on define on
prompt  ...done
