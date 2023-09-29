import {Observable} from "rxjs";

export declare interface RestSubscriber {
    subscribe(observable: Observable<Object>): void;
}

