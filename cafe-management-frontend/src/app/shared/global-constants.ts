export class GlobalConstants {

  //Paths
  public static homePath: string = "cafe";
  public static userPath: string = "user";
  public static dashboardPath: string = "dashboard";
  public static dashboardDetailsPath: string = "details";
  public static categoryPath: string = "category";
  public static productPath: string = "product";
  public static billPath: string = "bill";

  //Roles
  public static roleUser: string = "ROLE_USER"
  public static roleAdmin: string = "ROLE_ADMIN"
  public static roleAny: string = '';

  //Message
  public static genericError: string = "Something went wrong, please try again later!"
  public static unauthorized: string = "You are not unauthorized to access this page!"

  //Regex
  public static nameRegex: string = "[a-zA-Z0-9 ]*"
  public static emailRegex: string = "[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}"
  public static contactNumberRegex: string = "^[e0-9]{10,10}$"

  //Variable
  public static error: string = "error"
  public static success: string = "success"

}
