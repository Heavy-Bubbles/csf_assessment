export interface Post{
    id: string
    title: string
    postDate: bigint
    photo: string
    description: string
    tags: string[]
}

export interface TagCount{
    tag: string
    count: number
}