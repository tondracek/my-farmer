import {onDocumentWritten} from "firebase-functions/v2/firestore";
import * as logger from "firebase-functions/logger";

import {db, FieldValue} from "../config/firebase";
import {ShopEntity} from "../domain/ShopEntity";
import {CategoryPopularityEntity} from "../domain/CategoryPopularityEntity";

export const updateCategoryPopularity = onDocumentWritten(
    {
        document: "shop/{shopId}",
    },
    async (event) => {
        logger.info("Shop changed — updating category popularity.");

        // Pull typed docs
        const before = event.data?.before?.data() as ShopEntity | undefined;
        const after = event.data?.after?.data() as ShopEntity | undefined;

        // Extract category names (default to empty arrays)
        const beforeCats = before?.categories?.map((c) => c.name) ?? [];
        const afterCats = after?.categories?.map((c) => c.name) ?? [];

        const beforeSet = new Set(beforeCats);
        const afterSet = new Set(afterCats);

        // Compute differences
        const added = [...afterSet].filter((c) => !beforeSet.has(c));
        const removed = [...beforeSet].filter((c) => !afterSet.has(c));

        if (added.length === 0 && removed.length === 0) {
            logger.info("No category changes detected. Skipping.");
            return;
        }

        const batch = db.batch();
        const col = db.collection("category_popularity");

        // Increment counts for added categories
        for (const name of added) {
            const ref = col.doc(name);
            const update: Partial<CategoryPopularityEntity> = {
                count: FieldValue.increment(1) as any,
            };
            batch.set(ref, update, {merge: true});
        }

        // Decrement counts for removed categories
        for (const name of removed) {
            const ref = col.doc(name);
            const update: Partial<CategoryPopularityEntity> = {
                count: FieldValue.increment(-1) as any,
            };
            batch.set(ref, update, {merge: true});
        }

        const shopId = before?.id || after?.id || "unknown";
        logger.info(
            `Shop [${shopId}] Updated category popularity — added: [${added}], removed: [${removed}]`
        );

        return batch.commit();
    }
);
