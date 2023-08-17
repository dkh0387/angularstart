import {Injectable} from '@angular/core';

const imageAlts = ['hamster1', 'hamster2', 'hamster3'];

/*
Example of a service:
this one can be injected in any component, because of `providedIn: 'root'`.
We will use this one to transfer data from one component to another.
 */
@Injectable({
    providedIn: 'root'
})
export class FriendsAddingServiceService {
    get descriptions(): string[] {
        return this._descriptions;
    }

    get postTexts(): string[] {
        return this._postTexts;
    }

    get imageAlts(): string[] {
        return this._imageAlts;
    }

    get names(): string[] {
        return this._names;
    }

    get images(): string[] {
        return this._images;
    }

    private readonly _imgHamsterPath = 'assets/img/hamster/';
    private readonly ingFileType = '.jpg';
    private _names = ['Frederik', 'Mark', 'Denis']
    private _images = imageAlts.map(value => this._imgHamsterPath + value + this.ingFileType)
    private readonly _imageAlts = imageAlts;
    private _descriptions = ['3 Jahre alt', 'esse gerne Käse', 'bin sportlich']

    private _postTexts = ['Hallo! Mein Name ist Frederik, ich bin hier, um neue Freunde zu treffen!',
        'Hallo! Mein Name ist Marc, ich bin hier, um neue Freunde zu treffen!',
        'Hallo! Mein Name ist Denis, ich bin hier, um neue Freunde zu treffen!'
    ]

    constructor() {
    }

    addFriend(name: string, text: string, image: string) {
        this.names.push(name);
        this._postTexts.push(text);
        this._images.push(image);
    }
}
