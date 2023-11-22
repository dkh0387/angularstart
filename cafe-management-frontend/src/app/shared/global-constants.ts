export class GlobalConstants {

  //Paths
  public static homePath: string = "cafe";
  public static userPath: string = "user";
  public static dashboardPath: string = "dashboard";
  public static dashboardDetailsPath: string = "details";
  public static categoryPath: string = "category";
  public static productPath: string = "product";
  public static billPath: string = "bill";
  public static orderPath: string = "order";

  //Sidebar
  public static sidebarDashboard: string = "Dashboard";
  public static sidebarManageCategory: string = "Manage Category";
  public static sidebarManageProduct: string = "Manage Product";
  public static sidebarManageOrder: string = "Manage Order";
  public static sidebarViewBill: string = "View Bill";
  static sidebarManageUser: string = "Manage User";
  public static productIcon: string = "inventory_2";
  public static orderIcon: string = "shopping_cart";
  public static billIcon: string = "backup_table";
  public static userIcon: string = "people";

  //Roles
  public static roleUser: string = "ROLE_USER";
  public static roleAdmin: string = "ROLE_ADMIN";
  public static roleAny: string = '';

  //Message
  public static genericError: string = "Something went wrong, please try again later!";
  public static unauthorized: string = "You are not unauthorized to access this page!";
  public static productExistsError: string = "Product already exists!";
  public static productAdded = "Product added successfully!"

  //Regex
  public static nameRegex: string = "[a-zA-Z0-9 ]*";
  public static emailRegex: string = "[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}";
  public static contactNumberRegex: string = "^[e0-9]{10,10}$";
  public static priceRegex: string = "[0-9]+[^,]+(.[0-9]{1,2})?$";
  public static quantityRegex: string = "^[1-9][0-9]*$";
  public static statusRegex: string = "(true|false)$";

  //Variable
  public static error: string = "error";
  public static success: string = "success";
  public static defaultProductStatus: string = "false";

  //Dialog
  public static fullDialogWidth: string = "100%";
  public static dialogWidth: string = "550px";
  public static wideDialogWidth: string = "850px";

  //Actions
  public static dialogActionEdit: string = "Edit";
  public static dialogActionAdd: string = "Add";

  //Files
  public static fileExtensionPDF: string = ".pdf";

  /**
   * Lena specific stuff
   */
  public static RUS: string = "ru";
  public static GER: string = "de";

  // Routing
  public static aboutMePath: string = "aboutme";

  //Main page
  public static mainPageTitle: string = "FREIRAUM";
  public static mainPageIcon: string = "house";
  public static headerIconChangeLanguage: string = "language";

}
