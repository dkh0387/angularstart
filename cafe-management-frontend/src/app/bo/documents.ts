export class Documents {

    public name: any;
    public url: any;
    public description: any;

    constructor(name: string, url: string) {
        this.name = name;
        this.url = url;
    }

    setDescription(description: string) {
        this.description = description;
    }
}
