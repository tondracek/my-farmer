import {ShopCategoryEntity} from "./ShopCategoryEntity";

export interface ShopEntity {
    id?: string;
    name?: string | null;
    description?: string | null;
    ownerId?: string;
    categories?: ShopCategoryEntity[];
    images?: (string | null)[];
    // Other fields not included because Functions don't need them
}
