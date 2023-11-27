export class Documents {

    public name: any;
    public url: any;
    public description: any;
    public price: any;

    constructor(name: string, url: string, price: number) {
        this.name = name;
        this.url = url;
        this.price = price;
    }

    setDescription(description: string) {
        this.description = description;
    }
}
