import React from "react";
import {Card, Text, Group, Anchor, Badge} from "@mantine/core";
import {IconExternalLink} from "@tabler/icons-react";

interface SimilarHeadlineProps {
    title: string;
    link: string;
    summary: string;
    similarityScore: number;
    source: string;
}

export const SimilarHeadline: React.FC<SimilarHeadlineProps> = ({
                                                                    title,
                                                                    link,
                                                                    summary,
                                                                    similarityScore,
                                                                    source,
                                                                }) => {
    return (
        <Card shadow="sm" padding="lg" radius="md" m={10} withBorder>
            <Group position="apart" mb="md">
                <div>
                    <Text weight={500} size="lg">
                        {title}
                    </Text>
                    {source && (
                        <Text size="xs" color="dimmed">
                            Source: {source}
                        </Text>
                    )}
                </div>
                <Badge color="green" variant="filled">
                    Sim. {similarityScore}
                </Badge>
            </Group>
            <Text size="sm" color="dimmed" mb="md">
                {summary}
            </Text>
            <Anchor href={link} target="_blank" rel="noopener noreferrer"
                    style={{display: "flex", alignItems: "center"}}>
                <IconExternalLink size={16}/>
                <Text ml={4}>Read more</Text>
            </Anchor>
        </Card>
    );
};